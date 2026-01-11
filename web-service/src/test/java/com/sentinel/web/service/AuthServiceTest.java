//package com.sentinel.web.service;
//
//import com.sentinel.common.model.User;
//import com.sentinel.web.repository.AccountRepository;
//import com.sentinel.web.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//
//    @Mock private UserRepository userRepository;
//    @Mock private AccountRepository accountRepository; // Mocked because register creates an account
//    @Mock private PasswordEncoder passwordEncoder;
//
//    @InjectMocks private AuthService authService;
//
//    @Test
//    void shouldEncryptPasswordBeforeSaving() {
//        // Arrange
//        User rawUser = new User();
//        rawUser.setEmail("test@gmail.com");
//        rawUser.setPassword("secret123");
////
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // Email not taken
//        when(passwordEncoder.encode("secret123")).thenReturn("ENCRYPTED_HASH_XYZ"); // Fake encryption
//        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        // Act
//        User registeredUser = authService.register(rawUser);
//
//        // Assert
//        assertNotEquals("secret123", registeredUser.getPasswordHash()); // Should NOT be plain
//        assertEquals("ENCRYPTED_HASH_XYZ", registeredUser.getPasswordHash()); // Should be hashed
//
//        // Verify the repository was actually called
//        verify(userRepository).save(any(User.class));
//    }
//}