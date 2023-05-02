package mg.recipe.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import mg.recipe.recipe.Recipe;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> recipeList;

}
