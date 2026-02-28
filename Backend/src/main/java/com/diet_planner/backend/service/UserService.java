package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.User;
import com.diet_planner.backend.exception.NotFoundException;
import com.diet_planner.backend.exception.ValidationException;
import com.diet_planner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(User user, String rawPassword) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Invalid email or password"));

        if (user.getPasswordHash() == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ValidationException("Invalid email or password");
        }

        return user;
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
