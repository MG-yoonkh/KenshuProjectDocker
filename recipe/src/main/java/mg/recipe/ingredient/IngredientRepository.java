package mg.recipe.ingredient;

import mg.recipe.ingredientCategory.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository  extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findAllByCategoryId(Integer categoryId);
    List<Ingredient> findAllByNameContainingIgnoreCase(String name);

    List<Ingredient> findByNameContainingIgnoreCase(String name);

    List<Ingredient> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name);

    List<Ingredient> findAll();

    List<Ingredient> findAllByCategory(IngredientCategory ingredientCategory);
}
