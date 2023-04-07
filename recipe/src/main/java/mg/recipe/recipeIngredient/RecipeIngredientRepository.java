package mg.recipe.recipeIngredient;

import mg.recipe.instruction.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository  extends JpaRepository<RecipeIngredient, Integer> {
}
