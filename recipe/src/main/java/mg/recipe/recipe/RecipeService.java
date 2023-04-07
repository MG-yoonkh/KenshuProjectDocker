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

        if (orderBy.equals("date")) {
            sorts.add(Sort.Order.desc("createDate"));
        } else if (orderBy.equals("popular")) {
            sorts.add(Sort.Order.desc("voter"));
        }


        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));


        Specification<Recipe> spec = Specification.where(search(kw,category, cookTime, budget));
        if (spec == null) {
            return this.recipeRepository.findAll(pageable);
        }
        return this.recipeRepository.findAll(spec, pageable);
    }


    public Recipe getRecipe(Integer id){
        Optional<Recipe> recipe = this.recipeRepository.findById(id);
        if(recipe.isPresent()){
            return recipe.get();
        }else{
            throw new DataNotFoundException("レシピがありません。");
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

                query.distinct(true); // 重複 除去

                Join<Recipe, SiteUser> u1 = r.join("author", JoinType.LEFT);

                List<Predicate> predicates = new ArrayList<>(); // Predicate 리스트 생성

                if (kw != null && !kw.isEmpty()) {
                    Predicate kwPredicate = cb.or(
                            cb.like(r.get("recipeName"), "%" + kw + "%"),
                            cb.like(u1.get("username"), "%" + kw + "%")
                    );
                    predicates.add(kwPredicate); // 리스트에 추가
                }

                if (category != null && !category.isEmpty()) {
                    Predicate categoryPredicate = cb.equal(r.get("category"), category);
                    predicates.add(categoryPredicate); // 리스트에 추가
                }

                if (cookTime != null && !cookTime.isEmpty()) {
                    Predicate cookTimePredicate = cb.equal(r.get("cookTime"), cookTime);
                    predicates.add(cookTimePredicate); // 리스트에 추가
                }

                if (budget != null && !budget.isEmpty()) {
                    Predicate budgetPredicate = cb.equal(r.get("budget"), budget);
                    predicates.add(budgetPredicate); // 리스트에 추가
                }

                // 리스트가 비어있지 않으면 AND 연산 수행
                if (!predicates.isEmpty()) {
                    return cb.and(predicates.toArray(new Predicate[predicates.size()]));
                } else {
                    // 리스트가 비어있으면 true를 반환하여 모든 결과를 출력
                    return cb.isTrue(cb.literal(true));
                }
            }
        };
    }
}
