package mg.recipe.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @PostMapping("/api/check-duplicate")
//    public ResponseEntity<?> checkDuplicate(@RequestBody Map<String, String> body){
//        String username = body.get("username");
//        SiteUser user = userService.getUser(username);
//        System.out.println("1");
//        if(user != null){
//            System.out.println("既に利用中のIDです");
//            return ResponseEntity.badRequest().body(new ApiResponse(false, "既に利用中のIDです。"));
//        }else{
//            System.out.println("使用可能なIDです");
//            return ResponseEntity.ok(new ApiResponse(true,"使用可能なIDです。"));
//        }
//    }

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

//    @PostMapping("/login")
//    public String login() {
//        return "login";
//    }

    @GetMapping("/myPage")
    public String modifyUserForm(Model model, Principal principal){

        SiteUser user = this.userService.getUserByUsername(principal.getName());
        model.addAttribute("user",user);
        return "myPage";
    }
    @GetMapping("/myPage/reNickname")
    public String modifyNickNameForm(){
        return "reNickname";
    }
    @GetMapping("/myPage/rePassword")
    public String modifyPasswordForm(){
        return "rePassword";
    }
}
