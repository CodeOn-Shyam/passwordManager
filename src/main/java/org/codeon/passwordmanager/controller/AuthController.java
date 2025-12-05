package org.codeon.passwordmanager.controller;


import org.codeon.passwordmanager.dto.RegisterRequest;
import org.codeon.passwordmanager.dto.SetVaultKeyRequest;
import org.codeon.passwordmanager.model.Role;
import org.codeon.passwordmanager.model.User;
import org.codeon.passwordmanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.codeon.passwordmanager.dto.ChangePasswordRequest;
import org.codeon.passwordmanager.dto.ForgotPasswordRequest;
import org.codeon.passwordmanager.dto.SetRecoveryInfoRequest;
import org.codeon.passwordmanager.dto.ForgotVaultKeyRequest;

@RestController
@RequestMapping("/auth")
public class AuthController{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            return "Username already taken";
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(),encodedPassword,Role.ROLE_USER);
        userRepository.save(user);
        return "User registered successfully";
    }
    @PostMapping("/vault-key")
    public String setVaultKey(@RequestBody SetVaultKeyRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + username
                ));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid current password");
        }

        if (request.getVaultKey() == null || request.getVaultKey().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vault key cannot be empty");
        }

        String vaultKeyHash = passwordEncoder.encode(request.getVaultKey());
        user.setVaultKeyHash(vaultKeyHash);
        userRepository.save(user);

        return "Vault key set/updated successfully";
    }
    @PostMapping("/recovery-info")
    public String setRecoveryInfo(@RequestBody SetRecoveryInfoRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + username
                ));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid current password");
        }

        if (request.getQuestion() == null || request.getQuestion().isBlank()
                || request.getAnswer() == null || request.getAnswer().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question and answer are required");
        }

        user.setRecoveryQuestion(request.getQuestion());
        user.setRecoveryAnswerHash(passwordEncoder.encode(request.getAnswer()));
        userRepository.save(user);

        return "Recovery info set/updated successfully";
    }
    @PostMapping("/forgot-vault-key")
    public String forgotVaultKey(@RequestBody ForgotVaultKeyRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getRecoveryAnswer() == null || request.getRecoveryAnswer().isBlank()
                || request.getNewVaultKey() == null || request.getNewVaultKey().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username, recoveryAnswer and newVaultKey are required");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + request.getUsername()
                ));

        if (user.getRecoveryAnswerHash() == null || user.getRecoveryQuestion() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Recovery info not set for this user"
            );
        }

        if (!passwordEncoder.matches(request.getRecoveryAnswer(), user.getRecoveryAnswerHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid recovery answer"
            );
        }

        // Set new vault key
        String newVaultKeyHash = passwordEncoder.encode(request.getNewVaultKey());
        user.setVaultKeyHash(newVaultKeyHash);
        userRepository.save(user);

        return "Vault key reset successfully";
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + username
                ));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid current password");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getRecoveryAnswer() == null || request.getRecoveryAnswer().isBlank()
                || request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "username, recoveryAnswer and newPassword are required"
            );
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + request.getUsername()
                ));

        if (user.getRecoveryAnswerHash() == null || user.getRecoveryQuestion() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Recovery info not set for this user"
            );
        }

        if (!passwordEncoder.matches(request.getRecoveryAnswer(), user.getRecoveryAnswerHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid recovery answer"
            );
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password reset successfully";
    }
}