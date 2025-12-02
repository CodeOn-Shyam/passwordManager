package org.codeon.passwordmanager.controller;

import org.codeon.passwordmanager.model.Role;
import org.codeon.passwordmanager.dto.RegisterRequest;
import org.codeon.passwordmanager.model.User;
import org.codeon.passwordmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
}