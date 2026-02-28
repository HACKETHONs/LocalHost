package com.diet_planner.backend.dto.request;

import com.diet_planner.backend.entity.Goal;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    @Min(10)
    @Max(100)
    private Integer age;

    @NotNull
    @DecimalMin("100.0")
    @DecimalMax("250.0")
    private Double heightCm;

    @NotNull
    @DecimalMin("25.0")
    @DecimalMax("300.0")
    private Double weightKg;

    @NotNull
    private Goal goal;

    @NotNull
    @Min(1)
    @Max(6)
    private Integer mealsPerDay;

    @NotNull
    @Min(500)
    private Integer monthlyBudget;

    @NotNull
    private Boolean hasCookingAppliance;
}
