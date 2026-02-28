package com.diet_planner.backend.controller;

import com.diet_planner.backend.dto.request.UserRegistrationRequest;
import com.diet_planner.backend.dto.request.UserLoginRequest;
import com.diet_planner.backend.dto.response.UserResponse;
import com.diet_planner.backend.entity.User;
import com.diet_planner.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRegistrationRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().trim().toLowerCase())
                .age(request.getAge())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .goal(request.getGoal())
                .mealsPerDay(request.getMealsPerDay())
                .monthlyBudget(request.getMonthlyBudget())
                .hasCookingAppliance(request.getHasCookingAppliance())
                .build();

        User saved = userService.register(user, request.getPassword());

        return toResponse(saved);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody UserLoginRequest request) {
        User user = userService.login(request.getEmail().trim().toLowerCase(), request.getPassword());
        return toResponse(user);
    }

    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable UUID userId) {

        User user = userService.getById(userId);

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .goal(user.getGoal())
                .mealsPerDay(user.getMealsPerDay())
                .monthlyBudget(user.getMonthlyBudget())
                .build();
    }
}
