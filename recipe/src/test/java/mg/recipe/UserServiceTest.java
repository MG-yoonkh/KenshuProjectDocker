package mg.recipe;

import mg.recipe.user.SiteUser;
import mg.recipe.user.UserRepository;
import mg.recipe.user.UserRole;
import mg.recipe.user.UserService;
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

    @Test
    public void createTest(){
        // given
        String username = "yoon";
        String email = "yoon@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        // when
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(SiteUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // test
        SiteUser createUser = userService.create(username,email,password);

        assertEquals(username, createUser.getUsername());
        assertEquals(email, createUser.getEmail());
        assertEquals(UserRole.USER, createUser.getRole());
        assertEquals(encodedPassword, createUser.getPassword());
        assertNotNull(createUser.getCreateDate());
        assertNotNull(createUser.getModifyDate());

        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(SiteUser.class));
    }

    @Test
    public void existsByUsernameTest(){

        String existingUsername = "ExistingUser";
        String notExistingUsername = "NotExistingUser";

        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);
        when(userRepository.existsByUsername(notExistingUsername)).thenReturn(false);

        boolean existsForExistingUser = userService.existsByUsername(existingUsername);
        boolean existsForNotExistingUser = userService.existsByUsername(notExistingUsername);

        assertTrue(existsForExistingUser);
        assertFalse(existsForNotExistingUser);

        verify(userRepository, times(1)).existsByUsername(existingUsername);
        verify(userRepository, times(1)).existsByUsername(notExistingUsername);
    }

    @Test
    public void getUserByUsernameTest(){

        String existingUsername = "ExistingUser";
        String notExistingUsername = "NotExistingUser";

        // データの準備
        SiteUser existingUser = new SiteUser();
        existingUser.setUsername(existingUsername);
        existingUser.setEmail("existing.user@example.com");

        // 動作の設定
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(notExistingUsername)).thenReturn(Optional.empty());

        // テスト実行
        SiteUser retrivedUser = userService.getUserByUsername(existingUsername);

        // 結果の検証
        assertEquals(existingUser, retrivedUser);

        // リポジトリの呼び出しを確認
        verify(userRepository, times(1)).findByUsername(existingUsername);

        // 存在しないユーザーに対してException確認
        assertThrows(DataNotFoundException.class, () -> userService.getUserByUsername(notExistingUsername));

        // リポジトリの呼び出しを確認
        verify(userRepository, times(1)).findByUsername(notExistingUsername);
    }


}