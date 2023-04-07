package mg.recipe.ingredient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientSearchForm {

    private String name;
    private Long categoryId;

    private Integer ingredientId;

    private Double quantity;
    private Integer measurementUnitId;

}
