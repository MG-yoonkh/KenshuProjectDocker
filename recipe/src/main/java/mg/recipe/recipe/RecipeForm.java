package mg.recipe.recipe;

import java.util.ArrayList;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeForm {
    @NotEmpty(message = "タイトルは必須です。")
    @Size(max = 100)
    private String recipeName;

    private String ingredient;

    private String category;
    private String cookTime;
    private String budget;

    private String[] instruction;

    //private ArrayList<String> instruction;

    // @NotEmpty(message = "レシピイメージは必須です。")
    private String thumbnail;

    private String videoUrl;

    private Integer id;

}
