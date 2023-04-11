package mg.recipe.ingredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.ingredientCategory.IngredientCategory;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
