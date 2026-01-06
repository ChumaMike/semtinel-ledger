package com.sentinel.web.controller;

import com.sentinel.web.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", exposedHeaders = "Authorization")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> request) {
        String enteredPin = request.get("pin");

        // The 'Gatekeeper' logic
        if ("1234".equals(enteredPin)) {
            String token = jwtService.generateToken("SENTINEL_USER_01");
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "token", token
            ));
        }

        // If PIN is wrong, return 401 Unauthorized
        return ResponseEntity.status(401).body(Map.of(
                "status", "FAILED",
                "message", "Invalid Security PIN"
        ));
    }
}