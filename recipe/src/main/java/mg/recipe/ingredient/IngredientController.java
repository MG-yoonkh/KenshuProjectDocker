package mg.recipe.ingredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class IngredientController {

    private final RecipeService recipeService;


}
