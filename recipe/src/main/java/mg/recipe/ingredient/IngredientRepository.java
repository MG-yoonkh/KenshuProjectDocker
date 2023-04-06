package mg.recipe.ingredient;

import mg.recipe.instruction.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository  extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findAllByCategoryId(Integer categoryId);
    List<Ingredient> findAllByNameContainingIgnoreCase(String name);

    List<Ingredient> findByNameContainingIgnoreCase(String name);

    List<Ingredient> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name);
}
