package mg.recipe.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.recipe.Recipe;

@Getter
@Setter
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Integer id;
    @Column(length = 100, name = "ingredient_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_category_id")
    private IngredientCategory category;
}
