package mg.recipe.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.recipe.recipe.Recipe;
import mg.recipe.recipe.RecipeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RecipeService recipeService;

    @GetMapping("/signin")
    public String signin(UserCreateForm userCreateForm){
        return "signin";
    }
    @PostMapping("/signin")
    public String signin(@Valid UserCreateForm userCreateForm,
                         BindingResult bindingResult,
                         Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("firstErrorMessage", userService.getFirstErrorMessage(bindingResult));
            return "signin";
        }
        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "パスワードが一致していません。");
            model.addAttribute("firstErrorMessage", userService.getFirstErrorMessage(bindingResult));
            return "signin";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(),
                    userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed","既に登録されているIDです。");
            model.addAttribute("firstErrorMessage", userService.getFirstErrorMessage(bindingResult));
            return "signin";
        } catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            model.addAttribute("firstErrorMessage", userService.getFirstErrorMessage(bindingResult));
            return "signin";
        }
        return "redirect:/login";

    }
    @GetMapping("/signin/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam("username") String username) {
        boolean isDuplicate = userService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(@RequestParam(defaultValue = "0") int recipePage,
                         @RequestParam(defaultValue = "3") int recipeSize,
                         @RequestParam(defaultValue = "0") int likedPage,
                         @RequestParam(defaultValue = "3") int likedSize,
                         Model model,
                         Principal principal){

        SiteUser user = this.userService.getUserByUsername(principal.getName());

        Pageable recipePageable = PageRequest.of(recipePage, recipeSize, Sort.by("createDate").descending());
        Page<Recipe> recipePageResult = recipeService.findRecipesByAuthor(user, recipePageable);
        Pageable likedPageable = PageRequest.of(likedPage, likedSize, Sort.by("createDate").descending());
        Page<Recipe> likedRecipesPage = recipeService.findLikedRecipesByUserId(user.getId(), likedPageable);

        model.addAttribute("user", user);
        model.addAttribute("recipes", recipePageResult.getContent());
        model.addAttribute("recipePage", recipePage);
        model.addAttribute("recipeTotalPage", recipePageResult.getTotalPages());
        model.addAttribute("likedRecipes", likedRecipesPage.getContent());
        model.addAttribute("likedPage", likedPage);
        model.addAttribute("likedTotalPage", likedRecipesPage.getTotalPages());
        return "myPage";
    }


    @GetMapping("/mypage/repassword")
    public String updatePasswordForm(Model model,
                                     Principal principal){
        SiteUser user = this.userService.getUserByUsername(principal.getName());
        model.addAttribute("user",user);
        return "rePassword";
    }

    @PostMapping("/mypage/repassword")
    public String updatePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal){
        String username = principal.getName();

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordMismatchException("パスワードが一致しません。");
        }
        userService.updatePassword(username,currentPassword,newPassword);
        return "redirect:/mypage";
    }


    @GetMapping("/mypage/reemail")
    public String updateEmailForm(Model model,
                                  Principal principal){
        SiteUser user = this.userService.getUserByUsername(principal.getName());
        model.addAttribute("user",user);
        return "reEmail";
    }

    @PostMapping("/mypage/reemail")
    public String updateEmail(@RequestParam("newEmail") String newEmail,
                              Principal principal){
        String username = principal.getName();
        if(!EmailValidator.isValidEmail(newEmail)){
            throw new InvalidEmailFormatException("正しいe-mail形式ではありません。");
        }
        userService.updateEmail(username, newEmail);
        return "redirect:/mypage";
    }


    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam("newEmail") String email){
        Optional<SiteUser> optionalSiteUser = userService.getUserByEmail(email);
        return !optionalSiteUser.isPresent();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete_form")
    public String deleteUserForm(){
        return "deleteUser";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete")
    public String deleteUser(@RequestParam String username,
                             @RequestParam String password,
                             Principal principal,
                             Model model){
        SiteUser user = this.userService.getUserByUsername(principal.getName());
        if(user == null || !principal.getName().equals(username) || !userService.checkCredentials(user,password)){
            model.addAttribute("errorMessage", "IDとパスワードを確認してください。");
            return "deleteUser";
        }
        userService.deleteUser(user);
        return "redirect:/logout";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String grantAdminRole(){
        return "adminPage";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{userId}")
    public String grantAdminRole(@PathVariable("userId") Integer userId){
        userService.grantAdminRole(userId);
        return "redirect:/admin";
    }


    @GetMapping("/newpassword")
    public String newPasswordForm(){
        return "/newpassword";
    }

    @PostMapping("/newpassword")
    public String パスワード再設定(@Valid PasswordResetRequest passwordResetRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            String errorMessage = errors.get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return "redirect:/newpassword";
        }
        try {
            userService.resetPassword(passwordResetRequest.getUsername(), passwordResetRequest.getEmail(), passwordResetRequest.getPassword());
            redirectAttributes.addFlashAttribute("message", "パスワードが再設定されました。");
        } catch (InvalidUsernameException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/newpassword";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/newpassword";
        }
        return "redirect:/login";
    }

}
