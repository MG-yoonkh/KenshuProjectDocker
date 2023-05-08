package mg.recipe;

import jakarta.persistence.EntityNotFoundException;
import mg.recipe.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;



//    @Test
//    public void createTest(){
//        // given
//        String username = "yoon";
//        String email = "yoon@example.com";
//        String password = "password";
//        String encodedPassword = "encodedPassword";
//
//        // when
//        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
//        when(userRepository.save(any(SiteUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // test
//        SiteUser createUser = userService.create(username,email,password);
//
//        assertEquals(username, createUser.getUsername());
//        assertEquals(email, createUser.getEmail());
//        assertEquals(UserRole.USER, createUser.getRole());
//        assertEquals(encodedPassword, createUser.getPassword());
//        assertNotNull(createUser.getCreateDate());
//        assertNotNull(createUser.getModifyDate());
//
//        verify(passwordEncoder, times(1)).encode(password);
//        verify(userRepository, times(1)).save(any(SiteUser.class));
//    }
//
//    @Test
//    public void existsByUsernameTest(){
//
//        String existingUsername = "ExistingUser";
//        String notExistingUsername = "NotExistingUser";
//
//        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);
//        when(userRepository.existsByUsername(notExistingUsername)).thenReturn(false);
//
//        boolean existsForExistingUser = userService.existsByUsername(existingUsername);
//        boolean existsForNotExistingUser = userService.existsByUsername(notExistingUsername);
//
//        assertTrue(existsForExistingUser);
//        assertFalse(existsForNotExistingUser);
//
//        verify(userRepository, times(1)).existsByUsername(existingUsername);
//        verify(userRepository, times(1)).existsByUsername(notExistingUsername);
//    }
//
//    @Test
//    public void getUserByUsernameTest(){
//
//        String existingUsername = "ExistingUser";
//        String notExistingUsername = "NotExistingUser";
//
//        // データの準備
//        SiteUser existingUser = new SiteUser();
//        existingUser.setUsername(existingUsername);
//        existingUser.setEmail("existing.user@example.com");
//
//        // 動作の設定
//        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByUsername(notExistingUsername)).thenReturn(Optional.empty());
//
//        // テスト実行
//        SiteUser retrivedUser = userService.getUserByUsername(existingUsername);
//
//        // 結果の検証
//        assertEquals(existingUser, retrivedUser);
//
//        // リポジトリの呼び出しを確認
//        verify(userRepository, times(1)).findByUsername(existingUsername);
//
//        // 存在しないユーザーに対してException確認
//        assertThrows(DataNotFoundException.class, () -> userService.getUserByUsername(notExistingUsername));
//
//        // リポジトリの呼び出しを確認
//        verify(userRepository, times(1)).findByUsername(notExistingUsername);
//    }
//
//
//    private SiteUser user;
//    private Integer userId;
//    private UserRole role;
//    @BeforeEach
//    void setUp(){
//        userId = 1;
//        role = UserRole.ADMIN;
//        user = new SiteUser();
//        user.setId(userId);
//        user.setRole(UserRole.USER);
//    }
//    @Test
//    void changeUserRoleSuccess(){
//        // Arrange
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        // Act
//        userService.changeUserRole(userId, role);
//
//        // Assert
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).save(any(SiteUser.class));
//    }
//    @Test
//    void changeUserRoleFailure() {
//        // Arrange
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(EntityNotFoundException.class, () -> userService.changeUserRole(userId, role));
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(0)).save(any(SiteUser.class));
//    }

//    private SiteUser user;
//    private String username;
//    private String email;
//    private String newPassword;
//
//    @BeforeEach
//    void setUp() {
//        username = "testuser";
//        email = "testuser@example.com";
//        newPassword = "newPassword";
//
//        user = new SiteUser();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPassword("oldPassword");
//    }
//
//    @Test
//    void resetPasswordSuccess() {
//        // Arrange
//        when(userRepository.findByUsernameAndEmail(username, email)).thenReturn(Optional.of(user));
//        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
//
//        // Act
//        userService.resetPassword(username, email, newPassword);
//
//        // Assert
//        verify(userRepository, times(1)).findByUsernameAndEmail(username, email);
//        verify(passwordEncoder, times(1)).encode(newPassword);
//        verify(userRepository, times(1)).save(any(SiteUser.class));
//    }
//
//    @Test
//    void resetPasswordFailure() {
//        // Arrange
//        when(userRepository.findByUsernameAndEmail(username, email)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(InvalidUsernameException.class, () -> userService.resetPassword(username, email, newPassword));
//        verify(userRepository, times(1)).findByUsernameAndEmail(username, email);
//        verify(passwordEncoder, times(0)).encode(newPassword);
//        verify(userRepository, times(0)).save(any(SiteUser.class));
//    }


    private SiteUser user;
    private String username;
    private String currentPassword;
    private String newPassword;

    @BeforeEach
    void setUp(){
        username = "testuser";
        currentPassword = "currentPassword";
        newPassword = "newPassword";

        user = new SiteUser();
        user.setUsername(username);
        user.setPassword("encodedNewPassword");
    }

    @Test
    void updatePasswordSuccess() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // Act
        userService.updatePassword(username, currentPassword, newPassword);

        // Assert
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(currentPassword, user.getPassword());
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(any(SiteUser.class));
    }

    @Test
    void updatePasswordFailure() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(username, currentPassword, newPassword));
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(currentPassword, user.getPassword());
        verify(passwordEncoder, times(0)).encode(newPassword);
        verify(userRepository, times(0)).save(any(SiteUser.class));
    }

}
