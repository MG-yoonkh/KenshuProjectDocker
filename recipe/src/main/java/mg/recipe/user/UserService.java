package mg.recipe.user;

import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final SiteVisitRepository siteVisitRepository;

    public SiteUser create(String username, String email, String password){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(UserRole.USER);
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

    public void deleteUser(SiteUser user){
        userRepository.delete(user);
    }
    public boolean checkCredentials(SiteUser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }



    public void grantAdminRole(Integer userId) {
        SiteUser user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found with ID: " + userId));
        user.setRole(UserRole.ADMIN);
        userRepository.save(user);
    }

    public Map<YearMonth, Long> getMonthlyRegistrations(YearMonth startMonth, YearMonth endMonth) {
        List<Object[]> results = userRepository.countMonthlyRegistrations(startMonth, endMonth);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue()),
                        row -> ((Number) row[2]).longValue()
                ));
    }
    public Map<YearMonth, Long> getMonthlyVisitors(YearMonth startMonth, YearMonth endMonth) {
        List<Object[]> results = siteVisitRepository.countMonthlyVisitors(startMonth, endMonth);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue()),
                        row -> ((Number) row[2]).longValue()
                ));
    }
}
