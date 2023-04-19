package mg.recipe.ingredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.ingredientCategory.IngredientCategoryRepository;
import mg.recipe.ingredientCategory.IngredientCategoryService;
import mg.recipe.measurementUnit.MeasurementUnit;
import mg.recipe.measurementUnit.MeasurementUnitRepository;
import mg.recipe.measurementUnit.MeasurementUnitService;
import mg.recipe.recipe.Recipe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientCategoryService ingredientCategoryService;

    private final IngredientService ingredientService;

    private final MeasurementUnitService measurementUnitService;

    @GetMapping("/main")
    @ResponseBody
    public List<IngredientCategory> getMainList() {
        List<IngredientCategory> mainList = this.ingredientCategoryService.getMainList(0);
        return mainList;
    }

    @GetMapping("/unit")
    @ResponseBody
    public List<MeasurementUnit> getList() {
        List<MeasurementUnit> unitList = this.measurementUnitService.getList();
        return unitList;
    }

    // 詳細カテゴリリスト(by parentId from IngredientCategory)
    @PostMapping("/sub")
    @ResponseBody
    public List<IngredientCategory> getSubList(@RequestParam Integer ingredientCategoryId) {

        // ingredientCategoryId に対応する IngredientCategory を取得
        IngredientCategory category = this.ingredientCategoryService.getCategoryByCategoryId(ingredientCategoryId);
    
        // もし category が null だった場合、不正な ingredientCategoryId が指定されたとみなし、例外をスローする
        if (category == null) {
            throw new IllegalArgumentException("Invalid ingredient category id: " + ingredientCategoryId);
        }
    
        // category の親カテゴリーの ID を取得する
        Integer parentId = category.getParent() != null ? category.getParent().getId() : null;
        // もし parentId が null だった場合、category が親カテゴリーを持っていないとみなし、例外をスローする
        if (parentId == null) {
            throw new IllegalArgumentException("Ingredient category with id " + ingredientCategoryId + " has no parent.");
        }
    
        // parentId と level = 1 に対応するサブカテゴリーのリストを取得する
        List<IngredientCategory> subList = this.ingredientCategoryService.getSubList(parentId, 1);
    
        return subList;
    }

    // 材料リスト (by ingredientCategoryId from Ingredient)
    @PostMapping("/ing")
    @ResponseBody
    public List<Ingredient> getIngList(@RequestParam Integer categoryId) {
        System.out.println("categoryId/subcategoryId: " + categoryId);
        IngredientCategory ingredientCategory = this.ingredientCategoryService.getIngredientCategory(categoryId);
        List<Ingredient> iList = this.ingredientService.getSameCategoryIngredient(ingredientCategory);
        return iList;
    }

}
