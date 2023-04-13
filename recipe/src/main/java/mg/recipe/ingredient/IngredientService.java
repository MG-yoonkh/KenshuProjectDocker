package mg.recipe.ingredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.measurementUnit.MeasurementUnit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    // すべての材料リスト
    public List<Ingredient> getList() {
        return this.ingredientRepository.findAll();
    }

    // 同じカテゴリーの材料リスト
    public List<Ingredient> getSameCategoryIngredient(IngredientCategory categoryIngredient) {
        return this.ingredientRepository.findAllByCategory(categoryIngredient);
    }

    public Ingredient getIng(Integer id) {
        Optional<Ingredient> ing = ingredientRepository.findById(id);
        if(ing.isPresent()){
            return ing.get();
        }else{
            throw new DataNotFoundException("材料がありません。");
        }
    }

}
