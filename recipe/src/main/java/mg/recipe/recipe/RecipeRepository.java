package mg.recipe.recipe;

import mg.recipe.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Recipe findByRecipeName(String recipeName);
    Recipe findByRecipeNameAndCategory(String recipeName, String category);
    List<Recipe> findByRecipeNameLike(String recipeName);
    Page<Recipe> findAll(Pageable pageable);
    Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);

    Page<Recipe> findByAuthor(SiteUser author, Pageable pageable);

    @Query("SELECT r FROM Recipe r JOIN r.voter v WHERE v.id = :userId")
    Page<Recipe> findLikedRecipesByUserId(@Param("userId") Integer userId, Pageable pageable);

    int countByAuthorAndCreateDateBetween(SiteUser author, LocalDateTime startDate, LocalDateTime endDate);
}
