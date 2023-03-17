package mg.recipe.study;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="userInfo")
public class StudyUser { // 유저정보. VO
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;
}
