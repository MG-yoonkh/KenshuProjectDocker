package mg.recipe.recipe;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.ingredient.IngredientService;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.ingredientCategory.IngredientCategoryService;
import mg.recipe.instruction.InstructionService;
import mg.recipe.measurementUnit.MeasurementUnit;
import mg.recipe.measurementUnit.MeasurementUnitService;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final InstructionService instructionService;
    private final UserService userService;

    private final IngredientCategoryService ingredientCategoryService;

    private final IngredientService ingredientService;

    private final MeasurementUnitService measurementUnitService;
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    // メイン、検索結果ページ
    @GetMapping("/index")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value="kw", defaultValue = "") String kw,
                        @RequestParam(value="category", defaultValue = "") String category,
                        @RequestParam(value="cookTime", defaultValue = "") String cookTime,
                        @RequestParam(value="budget", defaultValue = "") String budget,
                        @RequestParam(value="orderBy", defaultValue = "date") String orderBy) {

        Page<Recipe> paging = this.recipeService.getList(page, kw, category, cookTime, budget, orderBy);

        model.addAttribute("paging", paging); // ページング
        model.addAttribute("kw", kw); // 検索キーワード
        model.addAttribute("category", category);
        model.addAttribute("cookTime", cookTime);
        model.addAttribute("budget", budget);
        model.addAttribute("orderBy", orderBy);
        return "index";
    }

    // レシピ詳細
    @GetMapping("/recipe/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipeDetail";
    }

    // レシピ登録機能
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/write")
    public String createRecipe(@Valid RecipeForm recipeForm,
                               @RequestParam("thumbFile") MultipartFile file,
                               BindingResult bindingResult,
                               Principal principal)throws IOException {

        if(bindingResult.hasErrors()){
            return "writeRecipe";
        }
        // イメージ登録
        Path fileStorageLocation = Paths.get("upload").toAbsolutePath().normalize();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // ファイルセーブする場所の生成
            Files.createDirectories(fileStorageLocation);

            //ファイルセーブ
            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation.toFile());

            // Recipe オブジェクトに経路を格納
            recipeForm.setThumbnail(targetLocation.toString());
        }catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
        }

        // レシピ登録
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Recipe recipe = this.recipeService.create(recipeForm,siteUser);

        // 材料を登録
//        this.ingredientService.create(recipe, recipeForm.getIngredient());
        // 調理方法を登録
//        this.instructionService.create(recipe, recipeForm.getInstruction());

        return "redirect:/index";
    }


    // レシピ登録画面
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/write")
    public String createRecipe(Model model, RecipeForm recipeForm){
        List<IngredientCategory> mainCategories = this.ingredientCategoryService.getMainList(0);
        if(mainCategories == null) {
            return "writeRecipe";
        }
        List<IngredientCategory> subCategories = this.ingredientCategoryService.getMainList(1);
        List<Ingredient> ingredients = this.ingredientService.getList();
        List<MeasurementUnit> units = this.measurementUnitService.getList();
        System.out.println("?: " + subCategories.get(0).getParent().getName().toString());
        model.addAttribute("mainCategories", mainCategories);
        model.addAttribute("subCategories", subCategories);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("units", units);
        return "writeRecipe";
    }


    @GetMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/modify/{id}")
    public String recipeModify(RecipeForm recipeForm, @PathVariable("id") Integer id,
                               Principal principal){
        Recipe recipe = this.recipeService.getRecipe(id);
        if(!recipe.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"修正権限がありません。");
        }
        recipeForm.setRecipeName(recipe.getRecipeName());
        return "writeRecipe";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/modify/{id}")
    public String recipeModify(@Valid RecipeForm recipeForm, BindingResult bindingResult,
                               Principal principal, @PathVariable Integer id){
        if(bindingResult.hasErrors()){
            return "writeRecipe";
        }
        Recipe recipe = this.recipeService.getRecipe(id);
        if(!recipe.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"修正権限がありません。");
        }
        this.recipeService.modify(recipe, recipeForm.getRecipeName());
        return String.format("redirect:/recipe/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/delete/{id}")
    public String recipeDelete(Principal principal, @PathVariable("id") Integer id) {
        Recipe recipe = this.recipeService.getRecipe(id);
        if(!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"削除権限がありません。");
        }
        this.recipeService.delete(recipe);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/vote/{id}")
    public String recipeVote(Principal principal, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.recipeService.vote(recipe,siteUser);
        return String.format("redirect:/recipe/detail/%s",id);
    }

}
