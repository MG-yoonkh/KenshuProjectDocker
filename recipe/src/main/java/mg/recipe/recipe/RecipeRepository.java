package mg.recipe.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Recipe findByRecipeName(String recipeName);
    Recipe findByRecipeNameAndCategory(String recipeName, String category);
    List<Recipe> findByRecipeNameLike(String recipeName);
    Page<Recipe> findAll(Pageable pageable);
    Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);

}
