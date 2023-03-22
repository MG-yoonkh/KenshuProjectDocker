package mg.recipe.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.Email;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "仕様書IDは必須です。")
    private String username;

    @NotEmpty(message = "パスワードは必須です。")
    private String password1;

    @NotEmpty(message = "パスワード確認は必須です。")
    private String password2;

    @NotEmpty(message = "e-mailは必須です。")
    private String email;
}
