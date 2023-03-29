package mg.recipe.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "ニックネームは必須です。")
    private String nickname;

    @NotEmpty(message = "パスワードは必須です。")
    private String password1;
    @NotEmpty(message = "パスワード確認は必須です。")
    private String password2;
    @NotEmpty(message = "e-mailは必須です。")
    @Email
    private String email;
}
