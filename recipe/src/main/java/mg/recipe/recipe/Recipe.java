package mg.recipe.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.ingredient.Recipe_Ingredients;
import mg.recipe.instruction.Instruction;
import mg.recipe.user.SiteUser;
//import mg.recipe.user.UserInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String recipeName;

    @Column(length = 20)
    private String category;

    private String cookTime;
    private String budget;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

//    private Integer calorie;
    @Column(length = 255)
    private String videoUrl;

    @Lob
    private String thumbnail;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Ingredient> ingredientList;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Instruction> instructionList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;


    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Recipe_Ingredients> recipeIngredients;
}
