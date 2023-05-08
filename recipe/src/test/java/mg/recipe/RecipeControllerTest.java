package mg.recipe;

import jakarta.servlet.http.HttpSession;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.ingredient.IngredientService;
import mg.recipe.ingredientCategory.IngredientCategory;
import mg.recipe.ingredientCategory.IngredientCategoryService;
import mg.recipe.instruction.Instruction;
import mg.recipe.instruction.InstructionService;
import mg.recipe.measurementUnit.MeasurementUnitService;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeController;
import mg.recipe.recipe.RecipeService;
import mg.recipe.recipeIngredient.RecipeIngredient;
import mg.recipe.recipeIngredient.RecipeIngredientService;
import mg.recipe.user.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest<MyClass> {

    @Mock
    private RecipeService recipeService;

    @Mock
    private InstructionService instructionService;

    @Mock
    private UserService userService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private MeasurementUnitService measurementUnitService;

    @Mock
    private RecipeIngredientService recipeIngredientService;

    @Mock
    private IngredientCategoryService ingredientCategoryService;

    @InjectMocks
    private RecipeController recipeController;

    private MockMvc mockMvc;

    @Test
    public void testIndex() {
        // Given
        int page = 0;
        String kw = "test";
        String category = "category";
        String cookTime = "30";
        String budget = "1000";
        String orderBy = "date";
        String message = "Welcome!";
        Page<Recipe> paging = mock(Page.class);
        when(recipeService.getList(eq(page), eq(kw), eq(category), eq(cookTime), eq(budget), eq(orderBy))).thenReturn(paging);
        Model model = mock(Model.class);

        // When
        String result = recipeController.index(model, page, kw, category, cookTime, budget, orderBy, message);

        // Then
        verify(recipeService).getList(eq(page), eq(kw), eq(category), eq(cookTime), eq(budget), eq(orderBy));
        verify(model).addAttribute(eq("paging"), eq(paging));
        verify(model).addAttribute(eq("kw"), eq(kw));
        verify(model).addAttribute(eq("category"), eq(category));
        verify(model).addAttribute(eq("cookTime"), eq(cookTime));
        verify(model).addAttribute(eq("budget"), eq(budget));
        verify(model).addAttribute(eq("orderBy"), eq(orderBy));
        verify(model).addAttribute(eq("message"), eq(message));
        verify(userService).createSiteVisit();
        assert(result.equals("index"));
    }

//    @Test
//    public void testDetail() throws Exception {
//        // Arrange
//        int recipeId = 1;
//        Recipe recipe = new Recipe();
//        recipe.setId(recipeId);
//
//        List<RecipeIngredient> riList = new ArrayList<>();
//        RecipeIngredient ri1 = new RecipeIngredient();
//        Ingredient i1 = new Ingredient();
//        i1.setName("Ingredient 1");
//        IngredientCategory ic1 = new IngredientCategory();
//        ic1.setName("Category 1");
//        i1.setCategory(ic1);
//        ri1.setIngredient(i1);
//        riList.add(ri1);
//
//        List<IngredientCategory> icList = new ArrayList<>();
//        IngredientCategory ic2 = new IngredientCategory();
//        ic2.setName("Category 2");
//        icList.add(ic2);
//
//        List<Instruction> istList = new ArrayList<>();
//        Instruction ist1 = new Instruction();
//        ist1.setDescription("Instruction 1");
//        istList.add(ist1);
//
//        when(recipeService.getRecipe(recipeId)).thenReturn(recipe);
//        when(recipeIngredientService.getAllIngredient(recipe)).thenReturn(riList);
//        when(ingredientCategoryService.getCategory(eq(ic1.getId()), anyInt())).thenReturn(ic2);
//        when(instructionService.getAllInstruction(recipe)).thenReturn(istList);
//        Model model = new ExtendedModelMap();
//        Principal principal = null;
//        HttpSession session = Mockito.mock(HttpSession.class);
//        session.setAttribute("recipeSaved", true);
//        session.getAttribute("recipeSaved");
//
//        // Act
//        String viewName = recipeController.detail(model, recipeId, principal, session);
//
//        // Assert
//        assertEquals("recipeDetail", viewName);
//        assertEquals(recipe, model.getAttribute("recipe"));
//        assertEquals(riList, model.getAttribute("riList"));
//        assertEquals(icList, model.getAttribute("icList"));
//        assertEquals(istList, model.getAttribute("istList"));
//        verify(session, times(1)).removeAttribute("recipeSaved");
//    }


}
