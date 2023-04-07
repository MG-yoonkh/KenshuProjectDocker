package mg.recipe.ingredientCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientCategoryService {

    private final IngredientCategoryRepository ingredientCategoryRepository;

    public List<IngredientCategory> getMainList(Integer level) {
        List<IngredientCategory> icList = this.ingredientCategoryRepository.findAllByLevel(level);

        if (icList == null) {
            System.out.println("null!!!");
            return null;
        }
        return icList;
    }

    // 詳細カテゴリ
    public List<IngredientCategory> getSubList(Integer parentId, Integer level) {
        List<IngredientCategory> icList = this.ingredientCategoryRepository.findByParentIdAndLevel(parentId, level);
        return icList;
    }

}
