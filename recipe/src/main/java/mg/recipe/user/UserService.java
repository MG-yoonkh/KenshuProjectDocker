package mg.recipe.user;

import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
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
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        } else{
            throw new DataNotFoundException("ユーザー情報が見つかりません。");
        }
    }

    public void updatePassword(String username, String currentPassword, String newPassword){
        SiteUser siteUser = getUserByUsername(username);
        if(passwordEncoder.matches(currentPassword, siteUser.getPassword())){
            siteUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(siteUser);
        }else {
            throw new InvalidPasswordException("パスワードが一致していません。");
        }
    }

    public Optional<SiteUser> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateEmail(String username, String newEmail){
        SiteUser siteUser = getUserByUsername(username);
        Optional<SiteUser> optionalSiteUser = getUserByEmail(newEmail);
        if(optionalSiteUser.isPresent() && !optionalSiteUser.get().getUsername().equals(siteUser.getUsername())){
            throw new EmailAlreadyExistsException("既に登録されたE-Mailです。");
        }
        siteUser.setEmail(newEmail);
        this.userRepository.save(siteUser);
    }
}
