package mg.recipe.recipeIngredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.instruction.InstructionService;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeService;
import mg.recipe.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeIngredientController {
    private final RecipeService recipeService;
    private final RecipeIngredientService recipeIngredientService;

    @GetMapping("/searchIngredient")
    public String searchIngredient() {
        return "searchIngredient";
    }

    @GetMapping("/getRiList")
    @ResponseBody
    public List<RecipeIngredient> getRiList(@RequestParam("recipeId") Integer id, Model model) {
        try {
            Recipe recipe = this.recipeService.getRecipe(id);
            List<RecipeIngredient> riList = this.recipeIngredientService.getAllIngredient(recipe);
            if (riList != null) {
                for (int i = 0; i < riList.size(); i++) {
                    System.out.println(riList.get(i));
                }
            }
            return riList;
        } catch (IllegalStateException ex) {
            // handle the exception
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error message", ex);
        }
    }
}
