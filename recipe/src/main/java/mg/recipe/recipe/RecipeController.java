package mg.recipe.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
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
            @RequestParam(value = "orderBy", defaultValue = "date") String orderBy) {

        Page<Recipe> paging = this.recipeService.getList(page, kw, category, cookTime, budget, orderBy);

        model.addAttribute("paging", paging); // ページング
        model.addAttribute("kw", kw); // 検索キーワード
        model.addAttribute("category", category);
        model.addAttribute("cookTime", cookTime);
        model.addAttribute("budget", budget);
        model.addAttribute("orderBy", orderBy);
        userService.createSiteVisit();
        return "index";
    }

    // レシピ詳細
    @GetMapping("/recipe/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Principal principal) {

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
            }
        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("riList", riList);
        return "recipeDetail";
    }

    // レシピ登録機能
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/write")
    public String createRecipe(@Valid RecipeForm recipeForm,
            @RequestParam("thumbFile") MultipartFile file,
            @RequestParam("sendList") String sendListStr,
            @RequestParam("description") String[] descriptionList,
            @RequestParam(value = "imgUrl") List<MultipartFile> files,
            BindingResult bindingResult,
            Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            return "writeRecipe";
        }

        // イメージ登録
        Path fileStorageLocation = Paths.get("C:", "KenshuProject", "recipe", "src", "main", "resources", "static", "uploaded")
        .toAbsolutePath();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());   
        
        // UUIDでランダムなファイル名を付与
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + "_" + fileName;

        try {
            // ファイルセーブする場所の生成
            Files.createDirectories(fileStorageLocation);

            // ファイルセーブ
            Path targetLocation = fileStorageLocation.resolve(newFileName);
            file.transferTo(targetLocation.toFile());

            // Recipe オブジェクトに経路を格納
            recipeForm.setThumbnail(newFileName);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
        }

        // レシピ登録
        SiteUser siteUser = this.userService.getUserByUsername(principal.getName());
        Recipe recipe = this.recipeService.create(recipeForm, siteUser);

        // Json -> Java
        ObjectMapper objectMapper = new ObjectMapper();
        List<RecipeIngredientJson> jsonList = objectMapper.readValue(sendListStr,
                new TypeReference<List<RecipeIngredientJson>>() {
                });

        // 材料を登録
        List<RecipeIngredient> rList = new ArrayList<>();
        for (RecipeIngredientJson json : jsonList) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(json.getQty());
            recipeIngredient.setMeasurementUnit(measurementUnitService.getUnit(json.getUnitId()));
            recipeIngredient.setIngredient(ingredientService.getIng(json.getIngredientId()));
            rList.add(recipeIngredient);
        }
        this.recipeIngredientService.create(recipe, rList);

        // イメージ登録
        // List<String> imgUrlList = new ArrayList<>();
        // System.out.println("1 name: " + files.get(0).getOriginalFilename() + "!");
        // System.out.println("2 name: " + files.get(1).getOriginalFilename() + "!");
        // System.out.println("3 name: " + files.get(2).getOriginalFilename() + "!");
        // for (int i = 0; i < files.size(); i++) {
        // String fileName2 = StringUtils.cleanPath(files.get(i).getOriginalFilename());

        // try {
        // // ファイルセーブする場所の生成
        // Files.createDirectories(fileStorageLocation);

        // // ファイルセーブ
        // Path targetLocation2 = fileStorageLocation.resolve(fileName2);
        // files.get(i).transferTo(targetLocation2.toFile());

        // // Recipe オブジェクトに経路を格納
        // imgUrlList.add(targetLocation2.toString());
        // System.out.println("imgUrl: " + imgUrlList.get(i).toString());

        // } catch (IOException e) {
        // throw new RuntimeException("Could not store file " + fileName2 + ". Please
        // try again!", e);
        // }
        // }

        // レシピ調理方法
        // List<Instruction> ist = new ArrayList<>();
        // for (int i = 0; i < descriptionList.length; i++) {
        // Instruction instruction = new Instruction();
        // instruction.setDescription(descriptionList[i]);
        // instruction.setImgUrl(imgUrlList.get(i));
        // instruction.setRecipe(recipe);
        // ist.add(instruction);
        // }
        // this.instructionService.create(ist);

        

        return String.format("redirect:/recipe/detail/%d", recipe.getId());
    }

    // レシピ登録画面
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/write")
    public String createRecipe(Model model, RecipeForm recipeForm) {
        return "writeRecipe";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/modify/{id}")
    public String recipeModify(RecipeForm recipeForm, @PathVariable("id") Integer id,
            Principal principal) {
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修正権限がありません。");
        }
        recipeForm.setRecipeName(recipe.getRecipeName());
        return "writeRecipe";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/modify/{id}")
    public String recipeModify(@Valid RecipeForm recipeForm,
            BindingResult bindingResult,
            Principal principal,
            @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            return "writeRecipe";
        }
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修正権限がありません。");
        }
        this.recipeService.modify(recipe, recipeForm.getRecipeName());
        return String.format("redirect:/recipe/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/delete/{id}")
    public String recipeDelete(Principal principal, @PathVariable("id") Integer id) {
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "削除権限がありません。");
        }
        this.recipeService.delete(recipe);
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

}
