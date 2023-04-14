package mg.recipe.admin;

import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeRepository;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserRepository;
import mg.recipe.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    public Page<SiteUser> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    public Page<Recipe> getRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
