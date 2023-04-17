package mg.recipe.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 10)
    @NotEmpty(message = "ニックネームは必須です。")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}$", message = "IDは3桁以上、10桁以下の英語大文字、小文字、数字で作成してください。")
    private String username;

    @NotEmpty(message = "パスワードは必須です。")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "パスワードは8~16字アルファベット、数字、特集文字を入力してください。")
    private String password1;
    @NotEmpty(message = "パスワード確認は必須です。")
    private String password2;
    @NotEmpty(message = "e-mailは必須です。")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "e-mail形式が正しくありません。")
    @Email
    private String email;
}
