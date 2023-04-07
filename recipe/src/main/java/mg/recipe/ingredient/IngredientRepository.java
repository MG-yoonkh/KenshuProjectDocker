package mg.recipe.ingredient;

import mg.recipe.instruction.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository  extends JpaRepository<Instruction, Integer> {
}
