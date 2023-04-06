package mg.recipe.measurementUnit;

import mg.recipe.instruction.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementUnitRepository extends JpaRepository<Instruction, Integer> {
}
