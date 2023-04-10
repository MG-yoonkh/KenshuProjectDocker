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

    // select  ingredient_category_id from INGREDIENT_CATEGORY where level = 0;
    List<IngredientCategory> findAllByLevel(Integer integer);

    // Query("SELECT ic FROM IngredientCategory ic WHERE ic.parentId = :parentId AND ic.level = :level")
    List<IngredientCategory> findByParentIdAndLevel(Integer parentId, Integer level);

    List<IngredientCategory> findByParentId(Integer parentId);

}
