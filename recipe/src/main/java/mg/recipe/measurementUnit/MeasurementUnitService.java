package mg.recipe.measurementUnit;

import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeasurementUnitService {

    private final MeasurementUnitRepository measurementUnitRepository;

    public List<MeasurementUnit> getList() {
        return measurementUnitRepository.findAll();
    }

    public MeasurementUnit getUnit(Integer id) {
        Optional<MeasurementUnit> unit = measurementUnitRepository.findById(id);
        if(unit.isPresent()){
            return unit.get();
        }else{
            throw new DataNotFoundException("単位がありません。");
        }
    }

}
