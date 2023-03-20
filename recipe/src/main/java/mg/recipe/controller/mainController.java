package mg.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class mainController {

    @GetMapping("/study")
    @ResponseBody
    public String study(){
        return "공부용 페이지입니다.감사합니다.";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signin")
    public String signin(){ return "signin"; }

    @GetMapping("/newpassword")
    public String newPassword(){
        return "newpassword";
    }

    @GetMapping("/myPage")
    public String mypage(){
        return "myPage";
    }

    @GetMapping("/writeRecipe")
    public String writeRecipe(){
        return "writeRecipe";
    }

    @GetMapping("/adminPage")
    public String adminPage(){
        return "adminPage";
    }
    @GetMapping("/withdrawal")
    public String withdrawal(){
        return "withdrawal";
    }
    @GetMapping("/recipeInfo")
    public String recipeInfo(){
        return "recipeInfo";
    }
    @GetMapping("/reNickname")
    public String reNickname(){
        return "reNickname";
    }
    @GetMapping("/rePassword")
    public String rePassword(){
        return "rePassword";
    }
    @GetMapping("/rePhoneNum")
    public String rePhoneNum(){
        return "rePhoneNum";
    }

    


}
