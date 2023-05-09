package mg.recipe.admin;

import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeRepository;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserRepository;
import mg.recipe.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private UserRepository userRepository;
    private RecipeRepository recipeRepository;
    private AdminService adminService;

    @BeforeEach
    void setUp(){
        userRepository = mock(UserRepository.class);
        recipeRepository = mock(RecipeRepository.class);
        adminService = new AdminService(userRepository, recipeRepository);
    }

    @Test
    void getUsersTest(){
        //Given
        SiteUser user1 = new SiteUser();
        SiteUser user2 = new SiteUser();
        List<SiteUser> users = Arrays.asList(user1, user2);
        Page<SiteUser> userPage = new PageImpl<>(users);

        Pageable pageable = PageRequest.of(0,2);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        //When
        Page<SiteUser> result = adminService.getUsers(pageable);

        //Then
        assertEquals(2, result.getNumberOfElements());
        assertEquals(user1, result.getContent().get(0));
        assertEquals(user2, result.getContent().get(1));

        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getRecipeTest(){
        //Given
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);
        Page<Recipe> recipePage = new PageImpl<>(recipes);

        Pageable pageable = PageRequest.of(0,2);

        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipePage);

        //When
        Page<Recipe> result = adminService.getRecipes(pageable);

        //Then
        assertEquals(2, result.getNumberOfElements());
        assertEquals(recipe1, result.getContent().get(0));
        assertEquals(recipe2, result.getContent().get(1));

        verify(recipeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void deleteUserTest(){
        //Given
        Integer userId = 1;

        //When
        adminService.deleteUser(userId);

        //Then
        verify(userRepository, times(1)).deleteById(userId);

    }

    @Test
    void deleteRecipeTest(){
        //Given
        Integer recipeId = 1;

        //When
        adminService.deleteRecipe(recipeId);

        //Then
        verify(recipeRepository, times(1)).deleteById(recipeId);
    }


}
