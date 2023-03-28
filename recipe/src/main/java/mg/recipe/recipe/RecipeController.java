package mg.recipe.recipe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<RecipeInfo> recipeInfoList = this.recipeRepository.findAll();
        model.addAttribute("recipeInfoList", recipeInfoList);
        return "index";
    }


}
