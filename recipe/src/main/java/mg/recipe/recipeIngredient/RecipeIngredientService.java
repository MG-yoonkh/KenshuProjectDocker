package mg.recipe.recipeIngredient;

import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecipeIngredientService {

    @Autowired
    private final RecipeIngredientRepository recipeIngredientRepository;
    public void create(Recipe recipe, List<RecipeIngredient> rList) {
        // 기존 레시피재료 삭제
        List<RecipeIngredient> irList = this.recipeIngredientRepository.findAllByRecipe(recipe);
        if(!irList.isEmpty()) {
            for (int i = 0; i < irList.size(); i++) {
                this.recipeIngredientRepository.delete(irList.get(i));
            }
        }
        // 새로운 레시피재료 등록
        RecipeIngredient ri;
        for (int i = 0; i < rList.size(); i++) {
            ri = new RecipeIngredient();
            ri.setIngredient(rList.get(i).getIngredient());
            ri.setQuantity(rList.get(i).getQuantity());
            ri.setMeasurementUnit(rList.get(i).getMeasurementUnit());
            ri.setRecipe(recipe);
            this.recipeIngredientRepository.save(ri);
        }
    }

    public List<RecipeIngredient> getAllIngredient(Recipe ri) {
        List<RecipeIngredient> irList = this.recipeIngredientRepository.findAllByRecipe(ri);
        if(irList.isEmpty()) {
            return null;
        } else {
            return irList;
        }
    }
}
