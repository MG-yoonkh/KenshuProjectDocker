package mg.recipe.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.ingredient.IngredientService;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.ingredientCategory.IngredientCategoryService;
import mg.recipe.instruction.Instruction;
import mg.recipe.instruction.InstructionService;
import mg.recipe.measurementUnit.MeasurementUnit;
import mg.recipe.measurementUnit.MeasurementUnitService;
import mg.recipe.recipeIngredient.RecipeIngredient;
import mg.recipe.recipeIngredient.RecipeIngredientJson;
import mg.recipe.recipeIngredient.RecipeIngredientService;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserService;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final InstructionService instructionService;
    private final UserService userService;

    private final IngredientService ingredientService;

    private final MeasurementUnitService measurementUnitService;

    private final RecipeIngredientService recipeIngredientService;
    private final IngredientCategoryService ingredientCategoryService;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    // メイン、検索結果ページ
    @GetMapping("/index")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw,
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "cookTime", defaultValue = "") String cookTime,
            @RequestParam(value = "budget", defaultValue = "") String budget,
            @RequestParam(value = "orderBy", defaultValue = "date") String orderBy,
            @ModelAttribute("message") String message) {

        Page<Recipe> paging = this.recipeService.getList(page, kw, category, cookTime, budget, orderBy);

        model.addAttribute("paging", paging); // ページング
        model.addAttribute("kw", kw); // 検索キーワード
        model.addAttribute("category", category);
        model.addAttribute("cookTime", cookTime);
        model.addAttribute("budget", budget);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("message", message);
        userService.createSiteVisit();
        return "index";
    }

    // レシピ詳細
    @GetMapping("/recipe/detail/{id}")
    public String detail(Model model,
                         @PathVariable("id") Integer id,
                         Principal principal,
                         HttpSession session) {

        Recipe recipe = this.recipeService.getRecipe(id);
        if (principal != null) {
            SiteUser siteUser = this.userService.getUserByUsername(principal.getName());
            boolean userHasVoted = recipe.hasUserVoted(siteUser);
            model.addAttribute("userHasVoted", userHasVoted);
        }

        // レシピ材料のリスト
        List<RecipeIngredient> riList = this.recipeIngredientService.getAllIngredient(recipe);
        if (riList != null) {
            for (int i = 0; i < riList.size(); i++) {
                System.out.println(riList.get(i));
//                IngredientCategory ic = this.ingredientCategoryService.getCategory(riList.get(i).getId(), 0);
            }
        }

        List<IngredientCategory> icList = new ArrayList<>();
        if (riList != null) {
            for (int i = 0; i < riList.size(); i++) {
                IngredientCategory ic = this.ingredientCategoryService.getCategory(riList.get(i).getIngredient().getCategory().getId(), 0);
                icList.add(ic);
                System.out.println(icList.get(i).getName());
            }
        }

        List<Instruction> istList = this.instructionService.getAllInstruction(recipe);

        model.addAttribute("recipe", recipe);
        model.addAttribute("riList", riList);
        model.addAttribute("icList", icList);
        model.addAttribute("istList", istList);

        Boolean recipeSaved = (Boolean) session.getAttribute("recipeSaved");

        if (recipeSaved == null || !recipeSaved) {
            model.addAttribute("expired", true);
        } else {
            session.removeAttribute("recipeSaved");
        }

        return "recipeDetail";
    }


    /**
     認証済みユーザーのみがアクセスできるエンドポイントです。レシピの投稿を作成します。
     @param recipeForm 投稿されたレシピのフォームデータ
     @param file 投稿されたサムネイル画像ファイル
     @param sendListStr レシピの送信先リスト（カンマ区切りのユーザーID文字列）
     @param descriptionList レシピの手順のテキスト配列
     @param files 調理手順の画像ファイルのリスト
     @param bindingResult フォームデータのバインド結果
     @param principal リクエストを行うユーザーのPrincipalオブジェクト
     @return リダイレクト先のURL文字列
     @throws IOException ファイルの処理中にエラーが発生した場合にスローされます
     */

    private static final int MAX_DAILY_RECIPES = 100;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/write")
    public String createRecipe(@Valid RecipeForm recipeForm,
                               @RequestParam("thumbFile") MultipartFile file,
                               @RequestParam("sendList") String sendListStr,
                               @RequestParam("description") String[] descriptionList,
                               @RequestParam(value = "imgUrl") List<MultipartFile> files,
                               Model model,
                               BindingResult bindingResult,
                               Principal principal,
                               HttpSession session) throws IOException {


        // エラーがある場合はレシピ投稿ページに戻る
        if (bindingResult.hasErrors()) {
            return "writeRecipe";
        }


        System.out.println("1: getRecipeName " + recipeForm.getRecipeName());
        System.out.println("2: getCategory " + recipeForm.getCategory());
        System.out.println("3: getThumbnail " + recipeForm.getThumbnail());
        System.out.println("4: getVideoUrl " + recipeForm.getVideoUrl());

        // サムネイル画像の保存
        String newFileName = saveThumbNail(file);

        // Recipe オブジェクトにファイルのパスをDBに格納
        recipeForm.setThumbnail(newFileName);

        // レシピの登録
        SiteUser siteUser = userService.getUserByUsername(principal.getName());
        Recipe recipe = recipeService.create(recipeForm, siteUser);

        int dailyRecipeCount = recipeService.countDailyRecipesByUser(siteUser, LocalDate.now());

        // 1 日に登録可能なレシピの数を超えていることを確認します。
        if (dailyRecipeCount >= MAX_DAILY_RECIPES) {
            model.addAttribute("error", "1 日に登録可能なレシピの数を超えました。");
            return "writeRecipe";
        }

        List<String> imgUrlList = saveInstructionImg(files);

        // レシピ調理手順の作成
        if (!imgUrlList.isEmpty()) {
            instructionService.create(descriptionList, imgUrlList, recipe);
        }

        // JSON文字列をJavaオブジェクトに変換する
        List<RecipeIngredient> rList = modifyJson(sendListStr);

        // 材料の登録
        recipeIngredientService.create(recipe, rList);



        session.setAttribute("recipeSaved", true);
        return String.format("redirect:/recipe/detail/%d", recipe.getId());
    }


    // レシピ登録画面
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/write")
    public String createRecipe(Model model,
                               RecipeForm recipeForm,
                               HttpSession session) {
        // List<IngredientCategory> mainList = this.ingredientCategoryService.getMainList(0);
        // model.addAttribute("mainList", mainList);
        Boolean recipeSaved = (Boolean) session.getAttribute("recipeSaved");

        if (recipeSaved != null && recipeSaved) {
            model.addAttribute("expired", true);
        } else {
            session.removeAttribute("recipeSaved");
        }


        return "writeRecipe";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/modify/{id}")
    public String recipeModify(Model model, RecipeForm recipeForm, @PathVariable("id") Integer id,
            Principal principal) {
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修正権限がありません。");
        }
        recipeForm.setRecipeName(recipe.getRecipeName());
        recipeForm.setThumbnail(recipe.getThumbnail());
        recipeForm.setCategory(recipe.getCategory());
        recipeForm.setCookTime(recipe.getCookTime());
        recipeForm.setBudget(recipe.getBudget());
        recipeForm.setId(recipe.getId());
        recipeForm.setVideoUrl("https://www.youtube.com/watch?v=" + recipe.getVideoUrl());

        // レシピ材料のリスト
        List<RecipeIngredient> riList = this.recipeIngredientService.getAllIngredient(recipe);

        List<IngredientCategory> icList = new ArrayList<>();
        if (riList != null) {
            for (int i = 0; i < riList.size(); i++) {
                IngredientCategory ic = this.ingredientCategoryService.getCategory(riList.get(i).getIngredient().getCategory().getId(), 0);
                icList.add(ic);
                System.out.println(icList.get(i).getName());
            }
        }


        List<Instruction> istList = this.instructionService.getAllInstruction(recipe);

        model.addAttribute("recipe", recipe);
        model.addAttribute("riList", riList);
        model.addAttribute("icList", icList);
        model.addAttribute("istList", istList);
        return "writeRecipe";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/modify/{id}")
    public String recipeModify(@Valid RecipeForm recipeForm,
                               @RequestParam("thumbFile") MultipartFile file,
                               @RequestParam("sendList") String sendListStr,
                               @RequestParam("description") String[] descriptionList,
                               @RequestParam(value = "imgUrl") List<MultipartFile> files,
                               BindingResult bindingResult,
                               Principal principal,
                               @PathVariable Integer id) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return "writeRecipe";
        }
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修正権限がありません。");
        }

        // サムネイル画像の保存
        String newFileName = saveThumbNail(file);

        // Recipe オブジェクトにファイルのパスをDBに格納
        recipeForm.setThumbnail(newFileName);

        this.recipeService.modify(recipe, recipeForm);
        System.out.println("1: getRecipeName " + recipeForm.getRecipeName());
        System.out.println("2: getCategory " + recipeForm.getCategory());
        System.out.println("3: getThumbnail " + recipeForm.getThumbnail());
        System.out.println("4: getVideoUrl " + recipeForm.getVideoUrl());

        // JSON文字列をJavaオブジェクトに変換する
        List<RecipeIngredient> rList = modifyJson(sendListStr);

        // 材料の登録
        if (rList != null && !rList.isEmpty()) {
            System.out.println(rList.get(0).getId());

        } else {
            System.out.println("rList is null or empty");
        }
        recipeIngredientService.modify(recipe, rList);


        // 調理手順の画像の登録
        List<String> imgUrlList = saveInstructionImg(files);

        // レシピ調理手順の作成
        if (!imgUrlList.isEmpty()) {
            instructionService.modify(descriptionList, imgUrlList, recipe);
        }


        return String.format("redirect:/recipe/detail/%s", id);
    }

    public List<RecipeIngredient> modifyJson(String sendListStr) throws JsonProcessingException {
        // JSON文字列をJavaオブジェクトに変換する
        ObjectMapper objectMapper = new ObjectMapper();

        List<RecipeIngredientJson> jsonList = objectMapper.readValue(sendListStr, new TypeReference<>() {});

        List<RecipeIngredient> rList = new ArrayList<>();
        for (RecipeIngredientJson json : jsonList) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(json.getQtyValue());

            if (json.getUnitValue() == null) {
                throw new IllegalArgumentException("UnitIDはnullにできません");
            }
            MeasurementUnit unit = measurementUnitService.getUnit(json.getUnitValue());
            if (unit == null) {
                throw new DataNotFoundException("Unitが見つかりません" + json.getUnitValue());
            }
            recipeIngredient.setMeasurementUnit(unit);

            if (json.getIngredientValue() == null) {
                throw new IllegalArgumentException("IngredientIDはnullにできません");
            }
            Ingredient ingredient = ingredientService.getIng(json.getIngredientValue());
            if (ingredient == null) {
                throw new DataNotFoundException("Ingredientが見つかりません " + json.getIngredientValue());
            }
            recipeIngredient.setIngredient(ingredient);

            rList.add(recipeIngredient);
        }
        return rList;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/delete/{id}")
    public String recipeDelete(Principal principal, @PathVariable("id") Integer id) {
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "削除権限がありません。");
        }
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                this.recipeService.delete(recipe);
                break;
            } catch (StaleObjectStateException e) {
                if (i == maxRetries - 1) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "同時に削除しようとしています。しばらく待ってからもう一度お試しください。");
                }
                // Refresh the entity from the database and try again
                recipe = this.recipeService.getRecipe(id);
            }
        }
        return "redirect:/";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/vote/{id}")
    public String recipeVote(Principal principal, @PathVariable("id") Integer id) {
        Recipe recipe = this.recipeService.getRecipe(id);
        SiteUser siteUser = this.userService.getUserByUsername(principal.getName());
        this.recipeService.handleVote(recipe, siteUser);
        return String.format("redirect:/recipe/detail/%s", id);
    }

    // 画像ファイルの保存先ディレクトリを設定
    // local
//    Path fileStorageLocation = Paths.get("recipe","src", "main", "resources", "static", "uploaded")
//            .toAbsolutePath();
    //docker
    Path fileStorageLocation = Paths.get("/app/img_files").toAbsolutePath().normalize();

    @GetMapping("/uploaded/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {

        // ファイルのパスを解決
        Path filePath = fileStorageLocation.resolve(filename).normalize();

        // リソースオブジェクトを作成
        Resource resource = new PathResource(filePath);

        // キャッシュ制御のためのHTTPヘッダーを設定
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        System.out.println("File storage location: " + fileStorageLocation.toString());

        // 画像ファイルのレスポンスエンティティを作成して返す
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    public String saveThumbNail(MultipartFile file) {

        // サムネイル画像の保存
        if (!file.isEmpty()) {

            // オリジナルファイル名をクリーンアップ
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // UUIDでランダムなファイル名を付
            UUID uuid = UUID.randomUUID();
            String newFileName = uuid.toString() + "_" + fileName;

            try {
                // ファイル保存する場所を作成
                Files.createDirectories(fileStorageLocation);

                // ファイル保存
                Path targetLocation = fileStorageLocation.resolve(newFileName);
                file.transferTo(targetLocation.toFile());

                System.out.println(fileStorageLocation);

                return newFileName;
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
            }
        } else {
            System.out.println("保存するThumbnailがありません。");
        }
        return null;
    }

    public List<String> saveInstructionImg(List<MultipartFile> files) {

        // 調理手順の画像の登録
        List<String> imgUrlList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            if (!files.get(i).isEmpty()) {

                // オリジナルファイル名をクリーンアップ
                String originalFilename = StringUtils.cleanPath(files.get(i).getOriginalFilename());
                UUID uuid = UUID.randomUUID();
                String fileName2 = uuid + "_" + originalFilename;

                try {
                    // ファイル保存する場所を作成
                    Files.createDirectories(fileStorageLocation);
                    // ファイル保存
                    Path targetLocation2 = fileStorageLocation.resolve(fileName2);
                    files.get(i).transferTo(targetLocation2.toFile());
                    imgUrlList.add(fileName2);

                    System.out.println(fileStorageLocation);

                } catch (IOException e) {
                    throw new RuntimeException("ファイルを保存できませんでした。 " + fileName2 + ". もう一度やり直してください!", e);
                }
            } else {
                imgUrlList.add("");
            }
        }

        return imgUrlList;
    }

}
