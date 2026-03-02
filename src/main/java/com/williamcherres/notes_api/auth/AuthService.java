package com.williamcherres.notes_api.auth;

import com.williamcherres.notes_api.dto.LoginRequest;
import com.williamcherres.notes_api.dto.RegisterRequest;
import com.williamcherres.notes_api.dto.UserResponse;
import com.williamcherres.notes_api.error.EmailAlreadyInUseException;
import com.williamcherres.notes_api.error.InvalidCredentialsException;
import com.williamcherres.notes_api.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public UserResponse register(RegisterRequest req) {
        String normalizedEmail = req.email().trim().toLowerCase();

        if (users.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyInUseException(normalizedEmail);
        }

        User u = new User();
        u.setEmail(normalizedEmail);
        u.setPasswordHash(encoder.encode(req.password()));
        u.setEmailVerified(false);

        User saved = users.save(u);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.isEmailVerified());
    }

    public UserResponse login(LoginRequest req) {
        String normalizedEmail = req.email().trim().toLowerCase();

        User user = users.findByEmail(normalizedEmail)
                .orElseThrow(InvalidCredentialsException::new);

        boolean ok = encoder.matches(req.password(), user.getPasswordHash());
        if (!ok) {
            throw new InvalidCredentialsException();
        }

        return new UserResponse(user.getId(), user.getEmail(), user.isEmailVerified());
    }
}