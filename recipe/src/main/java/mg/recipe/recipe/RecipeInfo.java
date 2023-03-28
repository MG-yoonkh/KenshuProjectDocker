package mg.recipe.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
//import mg.recipe.user.UserInfo;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class RecipeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String recipeName;

    @Column(length = 20)
    private String category;

    private Integer cooktime;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

//    private Integer calorie;
    @Column(length = 255)
    private String videoUrl;
    @Column(length = 255)
    private String thumbnail;
//    @ManyToOne
//    private UserInfo author;


}
