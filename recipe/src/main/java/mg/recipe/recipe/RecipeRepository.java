package mg.recipe.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Recipe findByRecipeName(String recipeName);

    Recipe findByRecipeNameAndCategory(String recipeName, String category);

    List<Recipe> findByRecipeNameLike(String recipeName);
}
