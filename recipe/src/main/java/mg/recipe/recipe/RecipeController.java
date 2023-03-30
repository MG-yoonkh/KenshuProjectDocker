package mg.recipe.recipe;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.ingredient.IngredientService;
import mg.recipe.instruction.InstructionService;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final InstructionService instructionService;
    private final UserService userService;
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value="kw", defaultValue = "") String kw) {
        Page<Recipe> paging = this.recipeService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "index";
    }

    @GetMapping("/recipeDetail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipeDetail";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/writeRecipe")
    public String createRecipe(@Valid RecipeForm recipeForm, BindingResult bindingResult,
                               Principal principal) {
        if(bindingResult.hasErrors()){
            return "writeRecipe";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Recipe r = this.recipeService.create(recipeForm.getRecipeName(),siteUser);
        this.ingredientService.create(r, recipeForm.getIngredient());
        this.instructionService.create(r, recipeForm.getInstruction());

        return "redirect:/index";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/writeRecipe")
    public String createRecipe(RecipeForm recipeForm){
        return "writeRecipe";
    }



    @GetMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/modify/{id}")
    public String recipeModify(RecipeForm recipeForm, @PathVariable("id") Integer id,
                               Principal principal){
        Recipe recipe = this.recipeService.getRecipe(id);
        if(!recipe.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"修正権限がありません。");
        }
        recipeForm.setRecipeName(recipe.getRecipeName());
        return "writeRecipe";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recipe/modify/{id}")
    public String recipeModify(@Valid RecipeForm recipeForm, BindingResult bindingResult,
                               Principal principal, @PathVariable Integer id){
        if(bindingResult.hasErrors()){
            return "writeRecipe";
        }
        Recipe recipe = this.recipeService.getRecipe(id);
        if(!recipe.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"修正権限がありません。");
        }
        this.recipeService.modify(recipe, recipeForm.getRecipeName());
        return String.format("redirect:/recipeDetail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/delete/{id}")
    public String recipeDelete(Principal principal, @PathVariable("id") Integer id) {
        Recipe recipe = this.recipeService.getRecipe(id);
        if(!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"削除権限がありません。");
        }
        this.recipeService.delete(recipe);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recipe/vote/{id}")
    public String recipeVote(Principal principal, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.recipeService.vote(recipe,siteUser);
        return String.format("redirect:/recipeDetail/%s",id);
    }


}
