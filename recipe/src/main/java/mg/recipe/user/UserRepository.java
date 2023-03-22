package mg.recipe.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<RecipeUser, Long> {

}
