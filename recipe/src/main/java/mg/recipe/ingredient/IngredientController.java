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
        System.out.println("%%%INGREDIENT_CATEGORY_ID: " + ingredientCategoryId);
        IngredientCategory category = this.ingredientCategoryService.getCategoryByCategoryId(ingredientCategoryId);
    
        if (category == null) {
            throw new IllegalArgumentException("Invalid ingredient category id: " + ingredientCategoryId);
        }
    
        Integer parentId = category.getParent() != null ? category.getParent().getId() : null;
        if (parentId == null) {
            throw new IllegalArgumentException("Ingredient category with id " + ingredientCategoryId + " has no parent.");
        }
    
        System.out.println("%%%parentId: " + parentId);
    
        List<IngredientCategory> subList = this.ingredientCategoryService.getSubList(parentId, 1);
    
        for (int i = 0; i < subList.size(); i++) {
            System.out.println("###IngredientCategory name: " + subList.get(i).getName());
        }
    
        return subList;
    }

    // 材料リスト (by ingredientCategoryId from Ingredient)
    @PostMapping("/ing")
    @ResponseBody
    public List<Ingredient> getIngList(@RequestParam Integer categoryId) {
        IngredientCategory ingredientCategory = this.ingredientCategoryService.getIngredientCategory(categoryId);
        List<Ingredient> iList = this.ingredientService.getSameCategoryIngredient(ingredientCategory);
        return iList;
    }

}
