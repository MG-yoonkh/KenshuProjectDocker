package mg.recipe.ingredient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeForm;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public void create(Recipe recipe, String ingredient) {
        Ingredient i = new Ingredient();
        i.setRecipe(recipe);
        i.setName(ingredient);
        this.ingredientRepository.save(i);
    }
}
