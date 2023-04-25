package mg.recipe.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeService;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserRole;
import mg.recipe.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RecipeService recipeService;
    private final AdminService adminService;
    private final SiteVisitRepository siteVisitRepository;

    @GetMapping("/admin")
    public String 管理者ページ(){
        return "adminPage";
    }
    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, Object>> グラフ() {
        Map<String, Object> data = new HashMap<>();

        YearMonth endMonth = YearMonth.now();
        YearMonth startMonth = endMonth.minusMonths(11);

        Map<YearMonth, Long> monthlyRegisteredUsers = userService.getMonthlyRegistrations(startMonth, endMonth);
        List<Object[]> formattedMonthlyRegisteredUsers = monthlyRegisteredUsers.entrySet().stream()
                .map(entry -> new Object[]{entry.getKey().toString(), entry.getValue()})
                .collect(Collectors.toList());
        data.put("monthlyRegisteredUsers", formattedMonthlyRegisteredUsers);

        Map<YearMonth, Long> monthlySiteVisits = userService.getMonthlyVisitors(startMonth,endMonth);
        List<Object[]> formattedMonthlySiteVisits = monthlySiteVisits.entrySet().stream()
                .map(entry -> new Object[]{entry.getKey().toString(), entry.getValue()})
                .collect(Collectors.toList());
        data.put("monthlySiteVisits", formattedMonthlySiteVisits);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/admin/user")
    public String ユーザー管理(@RequestParam(defaultValue = "0") int userpage,
                           @RequestParam(defaultValue = "10") int usersize,
                           Model model,
                           Pageable pageable){

        long totalUserCount = userService.getTotalUserCount();
        Page<SiteUser> users = adminService.getUsers(pageable);
        model.addAttribute("totalUserCount", totalUserCount);
        model.addAttribute("users", users.getContent());
        model.addAttribute("userpage",userpage);
        model.addAttribute("usersize",usersize);
        return "/userManagement";
    }
    @GetMapping("/admin/recipe")
    public String レシピ管理(Model model, Pageable pageable){
        long totalRecipeCount = recipeService.getTotalRecipeCount();
        Page<Recipe> recipes = adminService.getRecipes(pageable);
        Page<SiteUser> users = adminService.getUsers(pageable);
        model.addAttribute("totalRecipeCount", totalRecipeCount);
        model.addAttribute("recipes", recipes.getContent());
        return "/recipeManagement";
    }

    @PostMapping("/admin/user/{id}")
    public String deleteUser(@PathVariable("id") Integer userId){
        adminService.deleteUser(userId);
        return "redirect:/admin/user";
    }
    @PostMapping("/admin/recipe/{id}")
    public String deleteRecipe(@PathVariable("id") Integer recipeId){
        adminService.deleteRecipe(recipeId);
        return "redirect:/admin/recipe";
    }

    @GetMapping("/admin/adminright")
    public String adminRight(Model model){
        List<SiteUser> users = userService.findAll();
        model.addAttribute("users",users);
        return "adminRight";
    }

    @PostMapping("/admin/changeRole/{id}")
    public String changeUserRole(@PathVariable Integer id,
                                            @RequestParam UserRole role,
                                            RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserRole(id, role);
            redirectAttributes.addFlashAttribute("message","権限が変更されました。");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addAttribute("errorMessage",e.getMessage());
        }
        return "redirect:/admin/adminright";
    }

}
