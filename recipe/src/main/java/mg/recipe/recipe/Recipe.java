package mg.recipe.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.recipeIngredient.RecipeIngredient;
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
    @Column(name = "recipe_id")
    private Integer id;

    @Column(length = 100)
    private String recipeName;

    // 韓国料理・日本料理・中華料理・西洋料理
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

    // 作成者
    @ManyToOne
    private SiteUser author;

    // レシピをお気に入りしたユーザー
    @ManyToMany(fetch = FetchType.LAZY)
    Set<SiteUser> voter;

    // レシピ＆材料
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredientList;

    // 調理方法
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Instruction> instructionList;

}
