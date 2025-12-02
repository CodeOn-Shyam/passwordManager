package org.codeon.passwordmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.codeon.passwordmanager.repository.UserRepository;
import org.codeon.passwordmanager.model.User;
import org.codeon.passwordmanager.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootApplication
public class PasswordManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasswordManagerApplication.class, args);
    }
    @Bean
    CommandLineRunner initAdminUser(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        Role.ROLE_ADMIN
                );
                userRepository.save(admin);
                System.out.println("Default admin created: admin / admin123");
            }
        };
    }
}
