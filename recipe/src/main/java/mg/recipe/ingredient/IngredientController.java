package mg.recipe.ingredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.ingredientCategory.IngredientCategoryRepository;
import mg.recipe.ingredientCategory.IngredientCategoryService;
import mg.recipe.measurementUnit.MeasurementUnit;
import mg.recipe.measurementUnit.MeasurementUnitRepository;
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

    // 詳細カテゴリリスト(by parentId from IngredientCategory)
    @PostMapping("/sub")
    @ResponseBody
    public List<IngredientCategory> getSubList(@RequestParam Integer parentId) {
        List<IngredientCategory> icList = this.ingredientCategoryService.getSubList(parentId, 1);
        for(IngredientCategory vo : icList) {
            System.out.println(vo.getName());
        }
        return icList;
    }

    // 材料リスト (by ingredientCategoryId from Ingredient)
    @PostMapping("/ing")
    @ResponseBody
    public List<Ingredient> getIngList(@RequestParam Integer categoryId) {
        IngredientCategory ingredientCategory = this.ingredientCategoryService.getIngredientCategory(categoryId);
        List<Ingredient> iList = this.ingredientService.getSameCategoryIngredient(ingredientCategory);
        for(Ingredient vo : iList) {
            System.out.println(vo.getName());
        }
        return iList;
    }

    @PostMapping("/test")
    @ResponseBody
    public String handlePostRequest(@RequestParam String name) {
        System.out.println("Received name: " + name);
        return "success";
    }

}
