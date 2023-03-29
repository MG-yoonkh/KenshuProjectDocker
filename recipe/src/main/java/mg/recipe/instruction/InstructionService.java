package mg.recipe.instruction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.ingredient.IngredientRepository;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeForm;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructionService {

    private final InstructionRepository instructionRepository;

    public void create(Recipe recipe, String instruction) {
        Instruction i = new Instruction();
        i.setRecipe(recipe);
        i.setDescription(instruction);
        this.instructionRepository.save(i);
    }
}
