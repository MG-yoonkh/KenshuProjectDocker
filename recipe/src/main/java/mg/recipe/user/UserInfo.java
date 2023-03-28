package mg.recipe.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.recipe.RecipeInfo;

import java.time.LocalDateTime;
import java.util.List;

//@Getter
//@Setter
//@Entity
//public class UserInfo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    @Column(unique = true)
//    private String nickname;
//
//    @Column(unique = true)
//    private String email;
//    private String password;
//    private LocalDateTime createDate;
//    private LocalDateTime modifyDate;
//    private Integer authorize; // 권한
//
//    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
//    private List<RecipeInfo> recipeId;
//
//}
