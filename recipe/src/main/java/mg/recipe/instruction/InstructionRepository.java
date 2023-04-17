package mg.recipe.instruction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mg.recipe.recipe.Recipe;

public interface InstructionRepository extends JpaRepository<Instruction, Integer> {

    List<Instruction> findAllByRecipe(Recipe recipe);
}
