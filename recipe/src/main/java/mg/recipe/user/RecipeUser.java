package mg.recipe.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecipeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String password;
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickName;



}
