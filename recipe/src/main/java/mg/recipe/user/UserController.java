package mg.recipe.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

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

        userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(),
                userCreateForm.getPassword1());
        return "redirect:/";

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
