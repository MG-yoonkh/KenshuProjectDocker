package mg.recipe.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @NotEmpty(message = "IDは必須です。")
    @Size(min = 3, max = 10, message = "IDは3桁以上、10桁以下です。")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,10}$", message = "IDは英語大文字、小文字、数字で入力してください。")
    private String username;

    @NotEmpty(message = "E-Mailは必須です。")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "正しい形式で入力してください。")
    @Email
    private String email;

    @NotEmpty(message = "パスワードは必須です。")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",message = "特集文字は使用できません。")
    private String password;
}
