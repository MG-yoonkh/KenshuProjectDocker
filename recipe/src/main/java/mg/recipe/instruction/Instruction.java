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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
