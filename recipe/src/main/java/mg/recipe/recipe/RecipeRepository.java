package mg.recipe.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeInfo, Integer> {
    RecipeInfo findByRecipeName(String recipeName);
}
