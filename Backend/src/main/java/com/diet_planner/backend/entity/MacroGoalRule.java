package com.diet_planner.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "macro_goal_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MacroGoalRule {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "protein_weight")
    private Double proteinWeight;

    @Column(name = "carbs_weight")
    private Double carbsWeight;

    @Column(name = "fat_weight")
    private Double fatWeight;

    @Column(name = "sugar_weight")
    private Double sugarWeight;

    @Column(name = "min_protein_per_day")
    private Double minProteinPerDay;

    @Column(name = "max_calories_per_day")
    private Double maxCaloriesPerDay;
}