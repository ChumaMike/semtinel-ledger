package com.sentinel.web.controller;

import com.sentinel.web.model.User;
import com.sentinel.web.repository.UserRepository;
import com.sentinel.web.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> request) {
        String enteredPin = request.get("pin");

        return userRepository.findByPin(enteredPin)
                .map(user -> {
                    Map<String, Object> claims = Map.of(
                            "name", user.getFullName(),
                            "email", user.getEmail(),
                            "role", user.getRole()
                    );
                    String token = jwtService.generateToken(user.getUserId().toString(), claims);
                    return ResponseEntity.ok(Map.of(
                            "status", "SUCCESS",
                            "token", token,
                            "user", claims
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("message", "Invalid PIN")));
    }
}