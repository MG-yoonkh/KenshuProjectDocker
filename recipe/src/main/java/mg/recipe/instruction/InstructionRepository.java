package mg.recipe.instruction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction, Integer> {
}
