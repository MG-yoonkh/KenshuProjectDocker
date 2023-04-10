package mg.recipe.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUserById(Integer id){
        Optional<SiteUser> siteUser = this.userRepository.findById(id);
        if(siteUser.isPresent()){
            return siteUser.get();
        } else{
            throw new DataNotFoundException("ユーザー情報が見つかりません。");
        }
    }

    public SiteUser getUserByUsername(String username){
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        } else{
            throw new DataNotFoundException("ユーザー情報が見つかりません。");
        }
    }



    public void updateUser(Integer userId, SiteUser updateUser){
        SiteUser user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("ユーザー情報が見つかりません。"));

        user.setUsername(updateUser.getUsername());
        user.setEmail(updateUser.getEmail());
        user.setPassword(updateUser.getPassword());

        userRepository.save(user);
    }
}
