package mg.recipe.measurementUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementUnitService {

    private final MeasurementUnitRepository measurementUnitRepository;

    public List<MeasurementUnit> getList() {
        return measurementUnitRepository.findAll();
    }
}
