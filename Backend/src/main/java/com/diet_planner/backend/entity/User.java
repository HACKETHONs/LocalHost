package com.diet_planner.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private Integer age;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "meals_per_day")
    private Integer mealsPerDay;

    @Column(name = "monthly_budget")
    private Integer monthlyBudget;

    @Column(name = "has_cooking_appliance")
    private Boolean hasCookingAppliance;

    @Enumerated(EnumType.STRING)
    private Goal goal;
}