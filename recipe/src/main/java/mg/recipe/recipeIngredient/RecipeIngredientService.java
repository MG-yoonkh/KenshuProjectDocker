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
