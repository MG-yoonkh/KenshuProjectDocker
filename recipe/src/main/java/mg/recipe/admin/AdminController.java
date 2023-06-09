package mg.recipe.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeService;
import mg.recipe.user.SiteUser;
import mg.recipe.user.UserRole;
import mg.recipe.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final AdminService adminService;

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
    public String ユーザー管理(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "createDate"));
        Page<SiteUser> users = adminService.getUsers(pageable);
        model.addAttribute("totalUserCount", users.getTotalElements());
        model.addAttribute("users", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", page);
        return "userManagement";
    }


    @GetMapping("/admin/recipe")
    public String レシピ管理(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Recipe> recipes = adminService.getRecipes(pageable);

        model.addAttribute("totalRecipeCount", recipes.getTotalElements());
        model.addAttribute("recipes", recipes.getContent());
        model.addAttribute("totalPages",recipes.getTotalPages());
        model.addAttribute("currentPage",page);

        return "recipeManagement";
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
    public String adminRight(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "createDate"));
        Page<SiteUser> users = adminService.getUsers(pageable);
        model.addAttribute("totalUserCount", users.getTotalElements());
        model.addAttribute("users", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", page);
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
