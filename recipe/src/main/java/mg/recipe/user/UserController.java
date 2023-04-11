package mg.recipe.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/renickname")
    public String updateNicknameForm(Model model,
                                     Principal principal){
        SiteUser user = this.userService.getUserByUsername(principal.getName());
        model.addAttribute("user",user);
        return "reNickname";
    }
    @PostMapping("/mypage/renickname/{id}")
    public String updateNickname(@PathVariable("id") Integer id,
                                 @RequestParam("newNickname") String newNickname,
                                 BindingResult bindingResult,
                                 Principal principal
                                 ){
        if(bindingResult.hasErrors()){
            return "reNickname";
        }
        SiteUser user = this.userService.getUserById(id);
        if(!user.getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修正権限がありません。");
        }
        this.userService.updateNickname(user, newNickname);

        return String.format("redirect:/mypage/%s", id);
    }



    @GetMapping("/mypage/repassword")
    public String modifyPasswordForm(){
        return "rePassword";
    }
}
