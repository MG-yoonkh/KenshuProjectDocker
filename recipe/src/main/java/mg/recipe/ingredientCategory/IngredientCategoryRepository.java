package mg.recipe.ingredientCategory;

import mg.recipe.instruction.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientCategoryRepository  extends JpaRepository<IngredientCategory, Integer> {

    List<IngredientCategory> findAllByParentIdIsNull();

    IngredientCategory findParentById(Integer categoryId);

    List<IngredientCategory> findByParent(IngredientCategory parent);

}
