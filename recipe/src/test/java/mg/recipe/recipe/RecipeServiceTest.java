package mg.recipe.recipe;

import jakarta.persistence.criteria.*;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeForm;
import mg.recipe.recipe.RecipeRepository;
import mg.recipe.recipe.RecipeService;
import mg.recipe.recipeIngredient.RecipeIngredient;
import mg.recipe.recipeIngredient.RecipeIngredientRepository;
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
import java.util.Arrays;
import java.util.List;

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
    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

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



//    private Recipe recipe;
//    private List<RecipeIngredient> recipeIngredients;
//
//    @BeforeEach
//    void setUp(){
//        recipe = new Recipe();
//        recipe.setId(1);
//
//        RecipeIngredient ingredient1 = new RecipeIngredient();
//        ingredient1.setId(1);
//        RecipeIngredient ingredient2 = new RecipeIngredient();
//        ingredient2.setId(2);
//
//        recipeIngredients = Arrays.asList(ingredient1, ingredient2);
//    }
//
//    @Test
//    void delete(){
//        // Arrange
//        when(recipeIngredientRepository.findAllByRecipe(recipe)).thenReturn(recipeIngredients);
//
//        // Act
//        recipeService.delete(recipe);
//
//        // Assert
//        verify(recipeIngredientRepository, times(1)).findAllByRecipe(recipe);
//        verify(recipeIngredientRepository, times(1)).deleteById(1);
//        verify(recipeIngredientRepository, times(1)).deleteById(2);
//        verify(recipeRepository, times(1)).delete(recipe);
//    }




//    private CriteriaBuilder cb;
//    private CriteriaQuery<?> query;
//    private Root<Recipe> root;
//    private Join<Recipe, SiteUser> authorJoin;
//    private Join<Recipe, RecipeIngredient> ingredientsJoin;
//
//    @BeforeEach
//    void setUp() {
//        cb = mock(CriteriaBuilder.class);
//        query = mock(CriteriaQuery.class);
//        root = mock(Root.class);
//    }
//
//    @Test
//    void search(){
//        int page = 0;
//        String kw = "keyword";
//        String category = "category";
//        String cookTime = "cookTime";
//        String budget = "budget";
//        String orderBy = "popular";
//
//        RecipeService spyRecipeService = spy(recipeService);
//
//        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.desc("voterCount")));
//        Specification<Recipe> spec = mock(Specification.class);
//        Page<Recipe> expectedPage = mock(Page.class);
//
//        lenient().doReturn(spec).when(spyRecipeService).search(kw, category, cookTime, budget);
//        when(recipeRepository.findAll(spec, pageable)).thenReturn(expectedPage);
//
//        Page<Recipe> result = spyRecipeService.getList(page, kw, category, cookTime, budget, orderBy);
//
//        assertEquals(expectedPage, result);
//
//        verify(spyRecipeService).search(kw, category, cookTime, budget);
//        verify(recipeRepository).findAll(spec, pageable);
//    }





}
