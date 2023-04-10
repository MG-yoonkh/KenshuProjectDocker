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

    private final IngredientRepository ingredientRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final MeasurementUnitRepository measurementUnitRepository;

    private final IngredientCategoryService ingredientCategoryService;

    @GetMapping("/search")
    public String searchIngredients(Model model) {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientCategory> categories = ingredientCategoryRepository.findAll();
        List<MeasurementUnit> units = measurementUnitRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("units", units);
        model.addAttribute("searchForm", new IngredientSearchForm());

        return "searchIngredient";
    }

    @PostMapping("/search")
    public String searchIngredients(@ModelAttribute("searchForm") IngredientSearchForm searchForm,
                                    Model model) {
        List<Ingredient> ingredients;
        if (searchForm.getCategoryId() != null) {
            ingredients = ingredientRepository.findByCategoryIdAndNameContainingIgnoreCase(searchForm.getCategoryId(), searchForm.getName());
        } else {
            ingredients = ingredientRepository.findByNameContainingIgnoreCase(searchForm.getName());
        }
        List<IngredientCategory> categories = ingredientCategoryRepository.findAll();
        List<MeasurementUnit> units = measurementUnitRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("units", units);
        model.addAttribute("searchForm", searchForm);

        return "writeRecipe";
    }

    @GetMapping("/subcategories/{categoryId}")
    public List<IngredientCategory> getSubcategories(@PathVariable  Integer categoryId) {
        IngredientCategory parent = ingredientCategoryRepository.findParentById(categoryId);
        Integer parentId = parent.getId();

        Optional<IngredientCategory> optionalParent = ingredientCategoryRepository.findById(parentId);
        if (optionalParent.isPresent()) {
            IngredientCategory parent2 = optionalParent.get();
            List<IngredientCategory> childCategories = parent2.getChild();
            return childCategories;
        } else {
            return null;
        }

    }

    @PostMapping("/sub")
    @ResponseBody
    public List<IngredientCategory> getSubList(@RequestParam Integer parentId) {
        List<IngredientCategory> icList = this.ingredientCategoryService.getSubList(parentId, 1);
        for(IngredientCategory vo : icList) {
            System.out.println(vo.getName().toString());
        }
        return icList;
    }

    @PostMapping("/ing")
    @ResponseBody
    public List<IngredientCategory> getIngList(@RequestParam Integer subId) {
        List<IngredientCategory> icList = this.ingredientCategoryService.getSubList(subId, 1);
        for(IngredientCategory vo : icList) {
            System.out.println(vo.getName().toString());
        }
        return icList;
    }

    @PostMapping("/test")
    @ResponseBody
    public String handlePostRequest(@RequestParam String name) {
        System.out.println("Received name: " + name);
        return "success";
    }

}
