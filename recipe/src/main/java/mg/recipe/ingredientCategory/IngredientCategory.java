package mg.recipe.ingredientCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.ingredient.Ingredient;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ingredient_category")
public class IngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_category_id")
    private Integer id;

    // 単位
    @Column(name = "ingredient_category_name")
    private String ingredientCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private IngredientCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<IngredientCategory> child;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Ingredient> ingredient;

}
