package com.diet_planner.backend.service;

import com.diet_planner.backend.entity.User;
import com.diet_planner.backend.exception.NotFoundException;
import com.diet_planner.backend.exception.ValidationException;
import com.diet_planner.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        return userRepository.save(user);
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
