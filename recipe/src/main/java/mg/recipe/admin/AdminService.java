package mg.recipe.admin;

import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeRepository;
import mg.recipe.recipe.RecipeService;
import mg.recipe.recipeIngredient.RecipeIngredient;
import mg.recipe.recipeIngredient.RecipeIngredientRepository;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserRepository;
import mg.recipe.user.UserService;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeService recipeService;
    private final UserService userService;

    public Page<SiteUser> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    public Page<Recipe> getRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public void deleteUser(Integer userId) {
        SiteUser user = this.userService.getUser(userId);
        List<Recipe> votedRecipeList = this.recipeService.getAllRecipeByVoter(user);
        this.recipeService.deleteAllVote(votedRecipeList, user);
        List<Recipe> rList = this.recipeService.findAllRecipeByAuthor(user);
        for (Recipe recipe : rList) {
            try {
                this.recipeService.delete(recipe);
            } catch (StaleObjectStateException ex) {
                System.out.println("StaleObjectStateException caught while deleting recipe: " + ex.getMessage());
            }
        }
        userRepository.deleteById(userId);
    }
    public void deleteRecipe(Integer recipeId){
        Recipe recipe = this.recipeService.getRecipe(recipeId);
        try {
            List<RecipeIngredient> irList = this.recipeIngredientRepository.findAllByRecipe(recipe);
            for (int i = 0; i < irList.size(); i++) {
                System.out.println("delete: " + irList.get(i).getId());
                this.recipeIngredientRepository.deleteById(irList.get(i).getId());
            }
            this.recipeRepository.delete(recipe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
