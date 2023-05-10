package mg.recipe.recipeIngredient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientJson {
    private Integer ingredientValue;
    private String qtyValue;
    private Integer unitValue;

    private String subCategory;

    private String mainCategory;

    private String ingredient;
}
