package com.muyildirim.event_reservation.config;

import com.muyildirim.event_reservation.model.Role;
import com.muyildirim.event_reservation.model.User;
import com.muyildirim.event_reservation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            String email = "admin@example.com";

            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = new User();
                admin.setFullName("Admin");
                admin.setEmail(email);
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                System.out.println("✅ Default admin created: admin@example.com / admin123");
            } else {
                System.out.println("ℹ️ Admin already exists: admin@example.com");
            }
        };
    }
}
