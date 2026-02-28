package com.diet_planner.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "plan_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDay {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "day_number")
    private Integer dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    @OneToMany(
        mappedBy = "planDay",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Meal> meals = new ArrayList<>();
}