package mg.recipe.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.recipe.RecipeInfo;
import mg.recipe.user.UserInfo;

@Getter
@Setter
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100)
    private String name;
    private double quantity;
    @Column(length = 50)
    private String measure;
    @Column(length = 50)
    private String notes;

    @ManyToOne
    private RecipeInfo recipeInfo;
}
