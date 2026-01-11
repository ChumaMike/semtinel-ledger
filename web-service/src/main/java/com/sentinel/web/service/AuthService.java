package com.sentinel.web.service;

import com.sentinel.common.model.User; // <--- Ensure this matches where your User.java actually lives
import com.sentinel.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // Generates constructor for final fields (Best Practice)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        // 1. Check if email exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        // 2. Encrypt the password
        // Note: The User object must have the plain text password in 'passwordHash' initially
        // or a transient field. Assuming you passed plain text in 'passwordHash' for now:
        String plainPassword = user.getPasswordHash();
        String encodedPass = passwordEncoder.encode(plainPassword);

        user.setPasswordHash(encodedPass);

        // 3. Clear sensitive data if necessary (optional depending on flow)
        // user.setPinHash(passwordEncoder.encode(user.getPinHash())); // Handle PIN similarly

        // 4. Save
        return userRepository.save(user);
    }
}