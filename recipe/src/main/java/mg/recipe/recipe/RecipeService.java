package mg.recipe.recipe;

import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import mg.recipe.ingredient.Ingredient;
import mg.recipe.recipeIngredient.RecipeIngredient;
import mg.recipe.recipeIngredient.RecipeIngredientRepository;
import mg.recipe.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public Page<Recipe> getList(int page, String kw, String category, String cookTime, String budget, String orderBy){
        List<Sort.Order> sorts = new ArrayList<>();

        if (orderBy.equals("popular")) {
            sorts.add(Sort.Order.desc("voterCount"));
        } else {
            sorts.add(Sort.Order.desc("createDate"));
        }

        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));

        Specification<Recipe> spec = Specification.where(search(kw,category, cookTime, budget));

        return this.recipeRepository.findAll(spec, pageable);
    }

    public long getTotalRecipeCount(){
        return recipeRepository.count();
    }

    public Page<Recipe> findRecipesByAuthor(SiteUser author, Pageable pageable) {
        return recipeRepository.findByAuthor(author, pageable);
    }

    public Page<Recipe> findLikedRecipesByUserId(Integer userId, Pageable pageable) {
        return recipeRepository.findLikedRecipesByUserId(userId, pageable);
    }



    public Recipe getRecipe(Integer id){
        Optional<Recipe> recipe = this.recipeRepository.findById(id);
        if(recipe.isPresent()){
            return recipe.get();
        }else{
            throw new DataNotFoundException("レシピがありません。");
        }
    }

    public Recipe getRecipe(String recipeName) {
        Recipe recipe = this.recipeRepository.findByRecipeName(recipeName);
        return recipe;
    }
    public int countDailyRecipesByUser(SiteUser user, LocalDate date) {
        return recipeRepository.countByAuthorAndCreateDateBetween(user, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }
    public Recipe create(RecipeForm recipeForm, SiteUser user){
        Recipe r1 = new Recipe();
        r1.setRecipeName(recipeForm.getRecipeName());
        r1.setThumbnail(recipeForm.getThumbnail());
        r1.setCreateDate(LocalDateTime.now());
        r1.setModifyDate(LocalDateTime.now());
        r1.setAuthor(user);
        r1.setCategory(recipeForm.getCategory());
        r1.setCookTime(recipeForm.getCookTime());
        r1.setBudget(recipeForm.getBudget());

        String videoUrl = recipeForm.getVideoUrl();
        String videoId = extractVideoIdFromUrl(videoUrl);
        r1.setVideoUrl(videoId);

        //r1.setInstructionList(null);
        return this.recipeRepository.save(r1);
    }

    public String extractVideoIdFromUrl(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null; // URLから動画のIDが見つからない場合
        }
    }

    public void modify(Recipe recipe, @Valid RecipeForm recipeForm){
        recipe.setRecipeName(recipeForm.getRecipeName());
        if(recipeForm.getThumbnail() != null) {
            recipe.setThumbnail(recipeForm.getThumbnail());
        }
        recipe.setBudget(recipeForm.getBudget());
        recipe.setCategory(recipeForm.getCategory());
        recipe.setCookTime(recipeForm.getCookTime());

        recipe.setModifyDate(LocalDateTime.now());

        String videoUrl = recipeForm.getVideoUrl();
        String videoId = extractVideoIdFromUrl(videoUrl);
        recipe.setVideoUrl(videoId);

        this.recipeRepository.save(recipe);
    }

    public void delete(Recipe recipe) {
        try {
            List<RecipeIngredient> irList = this.recipeIngredientRepository.findAllByRecipe(recipe);
            for (int i = 0; i < irList.size(); i++) {
                System.out.println("delete: " + irList.get(i).getId());
                this.recipeIngredientRepository.deleteById(irList.get(i).getId());
            }
            this.recipeRepository.delete(recipe);
        } catch (Exception e) {
            // Log the error message and handle the exception here
            e.printStackTrace();
        }
    }



    public void handleVote(Recipe recipe, SiteUser siteUser) {
        if (recipe.hasUserVoted(siteUser)) {
            recipe.removeVoter(siteUser);
        } else {
            recipe.addVoter(siteUser);
        }

        recipeRepository.save(recipe);
    }


    private Specification<Recipe> search(String kw, String category, String cookTime, String budget) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Recipe> r, CriteriaQuery<?> query, CriteriaBuilder cb) {

                query.distinct(true); // 重複 除去

                Join<Recipe, SiteUser> u1 = r.join("author", JoinType.LEFT);
                r.join("ingredients", JoinType.LEFT);

                List<Predicate> predicates = new ArrayList<>(); // Predicate リスト生成

                if (kw != null && !kw.isEmpty()) {
                    Predicate kwPredicate = cb.or(
                            cb.like(r.get("recipeName"), "%" + kw + "%"),
                            cb.like(u1.get("username"), "%" + kw + "%"),
                            cb.like(r.join("ingredients", JoinType.LEFT).get("name"), "%" + kw + "%")
                    );
                    predicates.add(kwPredicate); // リスト追加
                }

                if (category != null && !category.isEmpty()) {
                    Predicate categoryPredicate = cb.equal(r.get("category"), category);
                    predicates.add(categoryPredicate); // リスト追加
                }

                if (cookTime != null && !cookTime.isEmpty()) {
                    Predicate cookTimePredicate = cb.equal(r.get("cookTime"), cookTime);
                    predicates.add(cookTimePredicate); // リスト追加
                }

                if (budget != null && !budget.isEmpty()) {
                    Predicate budgetPredicate = cb.equal(r.get("budget"), budget);
                    predicates.add(budgetPredicate); // リスト追加
                }

                // リストが空いていないとAND演算
                if (!predicates.isEmpty()) {
                    return cb.and(predicates.toArray(new Predicate[predicates.size()]));
                } else {
                    // リストが空いていたらTRUEを変換し、全ての結果を出力
                    return cb.isTrue(cb.literal(true));
                }
            }
        };
    }


}
