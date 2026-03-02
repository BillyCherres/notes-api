package com.williamcherres.notes_api.auth;

import com.williamcherres.notes_api.dto.RegisterRequest;
import com.williamcherres.notes_api.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        UserResponse created = service.register(req);
        return ResponseEntity.status(201).body(created);
    }
}