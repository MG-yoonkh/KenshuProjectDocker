package mg.recipe.recipe;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.instruction.Instruction;
import mg.recipe.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Page<Recipe> getList(int page, String kw, String category, String cookTime, String budget, String orderBy){
        List<Sort.Order> sorts = new ArrayList<>();

        if (orderBy.equals("new")) {
            sorts.add(Sort.Order.desc("createDate"));
        } else if (orderBy.equals("popular")) {
            sorts.add(Sort.Order.desc("voter"));
        }
        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
        Specification<Recipe> spec = Specification.where(search(kw,category, cookTime, budget));
        if (!kw.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(root.get("recipeName"), "%" + kw + "%"),
                    cb.like(root.get("author").get("username"), "%" + kw + "%")
            ));
        }

        if (!category.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }

        if (!cookTime.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("cookTime"), cookTime));
        }

        if (!budget.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("budget"), budget));
        }
        return this.recipeRepository.findAll(spec, pageable);
    }

    public Recipe getRecipe(Integer id){
        Optional<Recipe> recipe = this.recipeRepository.findById(id);
        if(recipe.isPresent()){
            return recipe.get();
        }else{
            throw new DataNotFoundException("레시피가 없습니다.");
        }
    }

    public Recipe create(RecipeForm recipeForm, SiteUser user){
        Recipe r1 = new Recipe();
        r1.setRecipeName(recipeForm.getRecipeName());
        r1.setThumbnail(recipeForm.getThumbnail());
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

    public void vote(Recipe recipe, SiteUser siteUser){
        recipe.getVoter().add(siteUser);
        this.recipeRepository.save(recipe);
    }


    private Specification<Recipe> search(String kw, String category, String cookTime, String budget) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Recipe> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); //重複 除去

                Join<Recipe, SiteUser> u1 = r.join("author", JoinType.LEFT);

                Predicate p = cb.conjunction(); // AND演算のためのPredicate

                // 検索ワードに関するレシピ名または作者名の条件追加
                if (!kw.isEmpty()) {
                    Predicate p1 = cb.like(r.get("recipeName"), "%" + kw + "%");
                    Predicate p2 = cb.like(r.get("author").get("username"), "%" + kw + "%");
                    p = cb.or(p1, p2);
                }

                // カテゴリに関する条件追加
                if (!category.isEmpty()) {
                    Predicate p3 = cb.equal(r.get("category"), category);
                    p = cb.and(p, p3);
                    return p;
                }

                // 調理時間に関する条件追加
                if (!cookTime.isEmpty()) {
//                    String[] range = cookTime.split("-");
                    Predicate p4 = cb.equal(r.get("cookTime"), cookTime);
                    p = cb.and(p, p4);
                    return p;
                }

                // 予算に関する条件追加
                if (!budget.isEmpty()) {
//                    String[] range = budget.split("-");
                    Predicate p5 = cb.equal(r.get("budget"), budget);
                    p = cb.and(p, p5);
                    return p;
                }



//                //Join<Recipe, Ingredient> ig = r.join("ingredientList", JoinType.LEFT);
                return cb.or(cb.like(r.get("recipeName"), "%" + kw + "%"),
                        cb.like(u1.get("username"), "%" + kw + "%")
                        );
            }
        };
    }
}
