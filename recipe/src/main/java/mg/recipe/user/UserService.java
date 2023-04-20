package mg.recipe.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mg.recipe.DataNotFoundException;
import mg.recipe.admin.SiteVisit;
import mg.recipe.admin.SiteVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
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
        user.setCreateDate(LocalDateTime.now());
        user.setModifyDate(LocalDateTime.now());
        this.userRepository.save(user);
        return user;
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
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
    public List<SiteUser> findAll() {
        return userRepository.findAll();
    }

    public void changeUserRole(Integer userId, UserRole role) {
        Optional<SiteUser> user = userRepository.findById(userId);

        if (user.isPresent()) {
            SiteUser targetUser = user.get();
            targetUser.setRole(role);
            userRepository.save(targetUser);
        } else {
            throw new EntityNotFoundException("ユーザーが見つかりません。");
        }
    }

    public void resetPassword(String username, String email, String newPassword) {

        Optional<SiteUser> userOptional = userRepository.findByUsernameAndEmail(username, email);
        if(userOptional.isEmpty()){
            throw new InvalidUsernameException("IDが存在しません。");
        }
        SiteUser user = userOptional.get();

        String encodePassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        userRepository.save(user);
    }


    public void updatePassword(String username, String currentPassword, String newPassword){
        SiteUser siteUser = getUserByUsername(username);
        if(passwordEncoder.matches(currentPassword, siteUser.getPassword())){
            siteUser.setPassword(passwordEncoder.encode(newPassword));
            siteUser.setModifyDate(LocalDateTime.now());
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
        siteUser.setModifyDate(LocalDateTime.now());
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

    public SiteVisit createSiteVisit() {
        SiteVisit siteVisit = new SiteVisit();
        siteVisit.setVisitDateTime(LocalDateTime.now());
        return siteVisitRepository.save(siteVisit);
    }

    public Map<YearMonth, Long> getMonthlyRegistrations(YearMonth startMonth, YearMonth endMonth) {
        LocalDateTime startDateTime = startMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = endMonth.plusMonths(1).atDay(1).atStartOfDay();
        List<Object[]> results = userRepository.countMonthlyRegistrations(startDateTime, endDateTime);
        System.out.println("getMonthlyRegistrations results: " + Arrays.deepToString(results.toArray()));
        System.out.println("getMonthlyRegistrations() called");
        return results.stream()
                .collect(Collectors.toMap(
                        row -> YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue()),
                        row -> ((Number) row[2]).longValue()
                ));
    }
    public Map<YearMonth, Long> getMonthlyVisitors(YearMonth startMonth, YearMonth endMonth) {
        LocalDateTime startDateTime = startMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = endMonth.plusMonths(1).atDay(1).atStartOfDay();
        List<Object[]> results = siteVisitRepository.countMonthlyVisitors(startDateTime, endDateTime);
        System.out.println("getMonthlyVisitors results: " + Arrays.deepToString(results.toArray()));
        System.out.println("getMonthlyVisitors() called");
        return results.stream()
                .collect(Collectors.toMap(
                        row -> YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue()),
                        row -> ((Number) row[2]).longValue()
                ));
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }
}
