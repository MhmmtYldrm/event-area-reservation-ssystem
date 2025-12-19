package com.muyildirim.event_reservation.service;

import com.muyildirim.event_reservation.model.Role;
import com.muyildirim.event_reservation.model.User;
import com.muyildirim.event_reservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(String fullName, String email, String rawPassword) {
        if (repo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(Role.USER);
        return repo.save(u);
    }

    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public User createAdminIfNotExists(String email, String password, String fullName) {
        return repo.findByEmail(email).orElseGet(() -> {
            User admin = new User();
            admin.setFullName(fullName);
            admin.setEmail(email);
            admin.setPassword(encoder.encode(password));
            admin.setRole(Role.ADMIN);
            return repo.save(admin);
        });
    }
}

