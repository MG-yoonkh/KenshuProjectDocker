package mg.recipe.domain;

import java.security.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String userPassword;

    @Column(length = 40)
    private String mailAddress;

    @Column(length = 20)
    private String nickName;

    @Column
    private LocalDateTime birthDate;

    @Column(length = 11)
    private int phoneNum;

    @Column(length = 200)
    private String profileImage;

    @Column(length = 1)
    private boolean delFlag;

    @Column
    private Timestamp updateTime;

}