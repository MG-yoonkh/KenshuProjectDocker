package mg.recipe.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/check-duplicate")
    public ResponseEntity<?> checkDuplicate(@RequestBody Map<String, String> body){
        String username = body.get("username");
        SiteUser user = userService.getUser(username);
        System.out.println("1");
        if(user != null){
            System.out.println("既に利用中のIDです");
            return ResponseEntity.badRequest().body(new ApiResponse(false, "既に利用中のIDです。"));
        }else{
            System.out.println("使用可能なIDです");
            return ResponseEntity.ok(new ApiResponse(true,"使用可能なIDです。"));
        }
    }

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

        userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(),
                userCreateForm.getPassword1());
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

}
