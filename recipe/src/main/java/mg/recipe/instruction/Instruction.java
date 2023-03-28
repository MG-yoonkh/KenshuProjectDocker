package mg.recipe.instruction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.recipe.Recipe;

@Getter
@Setter
@Entity
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer stepNum;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length = 255)
    private String imgUrl;
    @Column(length = 255)
    private String videoUrl;

    @ManyToOne
    private Recipe recipe;


}
