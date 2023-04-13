package mg.recipe.recipeIngredient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientJson {
    private String ingredient;
    private String unit;
    private String qty;
    private Integer ingredientId;
    private String qtyValue;
    private Integer unitId;
}
