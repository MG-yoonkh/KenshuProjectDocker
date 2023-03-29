package mg.recipe.recipe;

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

}
