package mg.recipe;

import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeForm;
import mg.recipe.recipe.RecipeRepository;
import mg.recipe.recipe.RecipeService;
import mg.recipe.user.SiteUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

//    @BeforeEach
//    void setUp(){
//    }
//
//    @Test
//    void getList(){
//        // Arrange
//        int page = 0;
//        String kw = "keyword";
//        String category = "category";
//        String cookTime = "20";
//        String budget = "1000";
//        String orderBy = "popular";
//
//        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
//                .thenReturn(Page.empty());
//
//        // Act
//        Page<Recipe> result = recipeService.getList(page, kw, category, cookTime, budget, orderBy);
//
//        // Assert
//        assertNotNull(result);
//    }



//    private RecipeForm recipeForm;
//    private SiteUser user;
//
//    @BeforeEach
//    void setUp(){
//        recipeForm = new RecipeForm();
//        recipeForm.setRecipeName("test Recipe");
//        recipeForm.setThumbnail("test Thumbnail");
//        recipeForm.setCategory("test Category");
//        recipeForm.setCookTime("20");
//        recipeForm.setBudget("1000");
//        recipeForm.setVideoUrl("https://www.youtube.com/watch?v=test_id");
//
//        user = new SiteUser();
//        user.setUsername("testUser");
//    }
//
//    @Test
//    void create(){
//        // Arrange
//        Recipe savedRecipe = new Recipe();
//        savedRecipe.setId(1);
//        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
//
//        // Act
//        Recipe result = recipeService.create(recipeForm, user);
//
//        // Assert
//        assertEquals(1, result.getId());
//        verify(recipeRepository, times(1)).save(any(Recipe.class));
//    }



//    private RecipeForm recipeForm;
//    private Recipe recipe;
//
//    @BeforeEach
//    void setUp(){
//        recipeForm = new RecipeForm();
//        recipeForm.setRecipeName("Modified Recipe");
//        recipeForm.setThumbnail("modified_thumbnail.jpg");
//        recipeForm.setCategory("Modified Category");
//        recipeForm.setCookTime("30");
//        recipeForm.setBudget("2000");
//        recipeForm.setVideoUrl("https://www.youtube.com/watch?v=modified_id");
//
//        recipe = new Recipe();
//        recipe.setId(1);
//        recipe.setCreateDate(LocalDateTime.now());
//        recipe.setModifyDate(LocalDateTime.now());
//        recipe.setAuthor(new SiteUser());
//    }
//
//    @Test
//    void modify(){
//        // Arrange
//        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
//
//        // Act
//        recipeService.modify(recipe, recipeForm);
//
//        // Assert
//        assertEquals("Modified Recipe", recipe.getRecipeName());
//        assertEquals("modified_thumbnail.jpg", recipe.getThumbnail());
//        assertEquals("Modified Category", recipe.getCategory());
//        assertEquals("30", recipe.getCookTime());
//        assertEquals("2000", recipe.getBudget());
//        assertEquals("modified_id", recipe.getVideoUrl());
//
//        verify(recipeRepository, times(1)).save(any(Recipe.class));
//    }





}
