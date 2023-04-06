package mg.recipe.recipeIngredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.measurementUnit.MeasurementUnit;
import mg.recipe.recipe.Recipe;

@Getter
@Setter
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_id")
    private Integer id;

    private Double quantity;

    @Column(length = 50)
    private String notes;

    @OneToOne
    @JoinColumn(name = "measurment_unit_id")
    private MeasurementUnit measurementUnit;

    @OneToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
