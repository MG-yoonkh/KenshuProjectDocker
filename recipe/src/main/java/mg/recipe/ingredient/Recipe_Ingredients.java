package mg.recipe.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.recipe.Recipe;
import mg.recipe.user.SiteUser;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Recipe_Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Recipe recipe;

    @ManyToOne
    private Measurment measurment;

    @ManyToOne
    private Ingredient ingredient;



}
