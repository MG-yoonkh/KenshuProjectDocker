package mg.recipe.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.ingredient.Ingredient;
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

    @Column(length = 255)
    private String videoUrl;

    @Lob
    private String thumbnail;

    // 作成者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private SiteUser author;



    // レシピをお気に入りしたユーザー
    @ManyToMany(fetch = FetchType.LAZY)
    Set<SiteUser> voter;

    public boolean hasUserVoted(SiteUser siteUser) {
        return this.voter.contains(siteUser);
    }
    public void addVoter(SiteUser siteUser) {
        this.voter.add(siteUser);
        this.voterCount = this.voter.size();
    }
    public void removeVoter(SiteUser siteUser) {
        this.voter.remove(siteUser);
        this.voterCount = this.voter.size();
    }

    private Integer voterCount;
    public Set<SiteUser> getVoter() {
        this.voterCount = this.voter.size();
        return this.voter;
    }
    public void setVoterCount(Set<SiteUser> voter){
        this.voterCount = voter.size();
    }


    @Version
    private int version;

    // レシピ＆材料
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredientList;


    // 調理方法
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Instruction> instructionList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Set<Ingredient> ingredients;

}
