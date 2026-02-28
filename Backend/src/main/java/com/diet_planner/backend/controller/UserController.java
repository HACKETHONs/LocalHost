package com.diet_planner.backend.controller;

import com.diet_planner.backend.dto.request.UserRegistrationRequest;
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
                .email(request.getEmail())
                .age(request.getAge())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .goal(request.getGoal())
                .mealsPerDay(request.getMealsPerDay())
                .monthlyBudget(request.getMonthlyBudget())
                .hasCookingAppliance(request.getHasCookingAppliance())
                .build();

        User saved = userService.register(user);

        return UserResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .goal(saved.getGoal())
                .mealsPerDay(saved.getMealsPerDay())
                .monthlyBudget(saved.getMonthlyBudget())
                .build();
    }

    @GetMapping("/{userId}")
    public UserResponse get(@PathVariable UUID userId) {

        User user = userService.getById(userId);

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
