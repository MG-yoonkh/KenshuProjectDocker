package mg.recipe.recipe;

import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import mg.recipe.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Page<Recipe> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
        return this.recipeRepository.findAll(pageable);
    }

    public Recipe getRecipe(Integer id){
        Optional<Recipe> recipe = this.recipeRepository.findById(id);
        if(recipe.isPresent()){
            return recipe.get();
        }else{
            throw new DataNotFoundException("레시피가 없습니다.");
        }
    }

    public Recipe create(String recipeName, SiteUser user){
        Recipe r1 = new Recipe();
        r1.setRecipeName(recipeName);
        r1.setCreateDate(LocalDateTime.now());
        r1.setAuthor(user);
        this.recipeRepository.save(r1);
        return r1;
    }

    public void modify(Recipe recipe, String recipeName){
        recipe.setRecipeName(recipeName);
        recipe.setModifyDate(LocalDateTime.now());
        this.recipeRepository.save(recipe);
    }

    public void delete(Recipe recipe) {
        this.recipeRepository.delete(recipe);
    }


}
