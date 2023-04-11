package mg.recipe.user;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signin")
    public String signin(UserCreateForm userCreateForm){
        return "signin";
    }
    @PostMapping("/signin")
    public String signin(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "signin";
        }
        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "パスワードが一致していません。");
            return "signin";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(),
                    userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed","既に登録されたIDです。");
            return "signin";
        } catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signin";
        }
        return "redirect:/login";

    }



    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String myPage(Model model, Principal principal){
        SiteUser user = this.userService.getUserByUsername(principal.getName());
        model.addAttribute("user",user);
        return "myPage";
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/mypage/renickname")
//    public String updateNicknameForm(Model model,
//                                     Principal principal){
//        SiteUser user = this.userService.getUserByUsername(principal.getName());
//        model.addAttribute("user",user);
//        return "reNickname";
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/mypage/renickname")
//    public String updateNickname(@RequestParam("newNickname") String newNickname,
//                                 Principal principal,
//                                 Model model,
//                                 HttpSession session
//                                 ){
//        SiteUser user = this.userService.getUserByUsername(principal.getName());
//        this.userService.updateNickname(user, newNickname);
//
//        // 닉네임 변경 후 인증 정보 갱신
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        SiteUser newUserDetails = new SiteUser();
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(newUserDetails, auth.getCredentials(), auth.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//
//        user.setUsername(newNickname);
//        session.setAttribute("user",user);
//        model.addAttribute("user",user);
//        return "myPage";
//    }



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
            throw new PasswordMismatchException("パスワードが一致していません。");
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

}
