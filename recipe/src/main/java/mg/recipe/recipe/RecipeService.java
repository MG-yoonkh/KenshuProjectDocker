package mg.recipe.recipe;

import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getList(){
        return this.recipeRepository.findAll();
    }

    public Recipe getRecipe(Integer id){
        Optional<Recipe> recipe = this.recipeRepository.findById(id);
        if(recipe.isPresent()){
            return recipe.get();
        }else{
            throw new DataNotFoundException("레시피가 없습니다.");
        }
    }

    public void create(String recipeName){
        Recipe r1 = new Recipe();
        r1.setRecipeName(recipeName);
        r1.setCreateDate(LocalDateTime.now());
        this.recipeRepository.save(r1);
    }
}
