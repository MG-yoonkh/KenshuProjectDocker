package mg.recipe.recipeIngredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.instruction.InstructionService;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeService;
import mg.recipe.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/getRecipe")
    @ResponseBody
    public List<RecipeIngredient> getRiList(@RequestParam Integer recipeId, Model model) {
        try {
            System.out.println("Received POST request with recipeId: " + recipeId);
            Recipe recipe = this.recipeService.getRecipe(recipeId);
            List<RecipeIngredient> riList = this.recipeIngredientService.getAllIngredient(recipe);
            if (riList != null) {
                for (int i = 0; i < riList.size(); i++) {
                    System.out.println(riList.get(i));
                }
            }
            return riList;
        } catch (IllegalStateException ex) {
            // handle the exception
            System.err.println("Exception caught in getRiList: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error message", ex);
        }
    }
}
