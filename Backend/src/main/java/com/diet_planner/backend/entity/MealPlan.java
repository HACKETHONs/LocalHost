package com.diet_planner.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "meal_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealPlan {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer month;
    private Integer year;

    private Integer budget;

    @Column(name = "meals_per_day")
    private Integer mealsPerDay;

    private Integer version;
    private String status;

    @OneToMany(
        mappedBy = "mealPlan",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<PlanDay> days = new ArrayList<>();
}