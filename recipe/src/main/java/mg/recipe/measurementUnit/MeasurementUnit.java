package mg.recipe.measurementUnit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "measurement_unit")
public class MeasurementUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measurement_unit_id")
    private Integer id;

    // 単位
    @Column(name = "measurement_unit_name")
    private String MeasurenentUnitName;

}
