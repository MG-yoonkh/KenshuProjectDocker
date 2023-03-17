package mg.recipe.study;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService { // 상속받은 UserRepository 인터페이스를 구현하는 클래스

    @Autowired
    private UserRepository userRepository;

    public StudyUser createUser(StudyUser user){
        return userRepository.save(user);
    }

    public StudyUser getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }
}
