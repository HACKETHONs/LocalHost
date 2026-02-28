package com.diet_planner.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private RecipeType type;

    @Column(name = "requires_appliance")
    private Boolean requiresAppliance;

    @Column(name = "avg_cost")
    private Integer avgCost;

    private Double protein;
    private Double calories;
}