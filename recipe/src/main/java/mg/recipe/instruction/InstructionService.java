package mg.recipe.instruction;

import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructionService {

    private final InstructionRepository instructionRepository;

    public void create(List<Instruction> ist) {

        for (int i = 0; i < ist.size(); i++) {

            this.instructionRepository.save(ist.get(i));
        }
    }

    public List<Instruction> getAllInstruction(Recipe recipe) {
        List<Instruction> istList = this.instructionRepository.findAllByRecipe(recipe);
        if (istList.isEmpty()) {
            return null;
        } else {
            return istList;
        }
    }

    public void create(String[] descriptionList, List<String> imgUrlList, Recipe recipe) {
        Instruction instruction = new Instruction();

        for (int i = 0; i < imgUrlList.size(); i++) {
            instruction = new Instruction();
            instruction.setDescription(descriptionList[i]);
            instruction.setImgUrl(imgUrlList.get(i).toString());
            instruction.setRecipe(recipe);
            this.instructionRepository.save(instruction);
        }
    }
}
