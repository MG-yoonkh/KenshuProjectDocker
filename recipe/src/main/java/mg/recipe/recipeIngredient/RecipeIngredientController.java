package mg.recipe.recipeIngredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.instruction.InstructionService;
import mg.recipe.recipe.RecipeService;
import mg.recipe.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class RecipeIngredientController {
    private final RecipeService recipeService;
    private final RecipeIngredientService recipeIngredientService;

    @GetMapping("/searchIngredient")
    public String searchIngredient() {
        return "searchIngredient";
    }

}
