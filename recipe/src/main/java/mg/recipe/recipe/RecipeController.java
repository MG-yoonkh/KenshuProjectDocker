package mg.recipe.recipe;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.ingredient.IngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<Recipe> recipeList = this.recipeService.getList();
        model.addAttribute("recipeList", recipeList);
        return "index";
    }

    @GetMapping("/recipeDetail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipeDetail";
    }

    @PostMapping("/writeRecipe")
    public String createRecipe(@Valid RecipeForm recipeForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "writeRecipe";
        }
        //this.recipeService.create(recipeForm.getRecipeName()); //recipe id
        Recipe r = this.recipeService.create(recipeForm.getRecipeName());
        this.ingredientService.create(r, recipeForm.getIngredient());
        //this.instructionService.create(recipe, recipeForm);



        return "redirect:/index";
    }

    @GetMapping("/writeRecipe")
    public String createRecipe(RecipeForm recipeForm){
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

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
