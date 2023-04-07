package mg.recipe.ingredient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getList() {
        return this.ingredientRepository.findAll();
    }

}
