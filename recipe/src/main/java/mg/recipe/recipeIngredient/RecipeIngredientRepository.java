package mg.recipe.recipeIngredient;

import mg.recipe.instruction.Instruction;
import mg.recipe.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository  extends JpaRepository<RecipeIngredient, Integer> {
    List<RecipeIngredient> findAllByRecipe(Recipe ri);
}
