package mg.recipe.instruction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructionService {

    private final InstructionRepository instructionRepository;

//    public void create(Recipe recipe, String instruction) {
//        Instruction i = new Instruction();
//        i.setRecipe(recipe);
//        i.setDescription(instruction);
//        this.instructionRepository.save(i);
//    }
}
