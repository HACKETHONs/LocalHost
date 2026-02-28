package com.diet_planner.backend.dto.response;

import com.diet_planner.backend.entity.Goal;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;

    private Goal goal;
    private Integer mealsPerDay;
    private Integer monthlyBudget;
}