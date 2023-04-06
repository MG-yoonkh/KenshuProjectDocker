package mg.recipe.ingredientCategory;

import mg.recipe.instruction.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientCategoryRepository  extends JpaRepository<Instruction, Integer> {
}
