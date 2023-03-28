package mg.recipe.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<Recipe> recipeList = this.recipeService.getList();
        model.addAttribute("recipeList", recipeList);
        return "index";
    }

    @GetMapping("/recipeDetail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Recipe recipe = this.recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipeDetail";
    }
}
