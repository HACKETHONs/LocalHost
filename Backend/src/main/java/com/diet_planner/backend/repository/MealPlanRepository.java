package com.diet_planner.backend.repository;

import com.diet_planner.backend.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MealPlanRepository extends JpaRepository<MealPlan, UUID> {

    Optional<MealPlan> findTopByUserIdAndMonthAndYearOrderByVersionDesc(UUID userId, Integer month, Integer year);
}
