package mg.recipe.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeInfo, Integer> {
    RecipeInfo findByRecipeName(String recipeName);

    RecipeInfo findByRecipeNameAndCategory(String recipeName, String category);

    List<RecipeInfo> findByRecipeNameLike(String recipeName);
}
