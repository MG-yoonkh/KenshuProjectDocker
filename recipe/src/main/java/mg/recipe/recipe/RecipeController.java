package mg.recipe.recipe;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.ingredient.IngredientService;
import mg.recipe.instruction.InstructionService;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final InstructionService instructionService;
    private final UserService userService;
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Recipe> paging = this.recipeService.getList(page);
        model.addAttribute("paging", paging);
        return "index";
    }

    @GetMapping("/recipeDetail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipeDetail";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/writeRecipe")
    public String createRecipe(@Valid RecipeForm recipeForm, BindingResult bindingResult,
                               Principal principal) {
        if(bindingResult.hasErrors()){
            return "writeRecipe";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Recipe r = this.recipeService.create(recipeForm.getRecipeName(),siteUser);
        this.ingredientService.create(r, recipeForm.getIngredient());
        this.instructionService.create(r, recipeForm.getInstruction());

        return "redirect:/index";
    }
    @PreAuthorize("isAuthenticated()")
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




}
