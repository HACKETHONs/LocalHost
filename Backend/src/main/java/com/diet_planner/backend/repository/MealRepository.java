package com.diet_planner.backend.repository;

import com.diet_planner.backend.entity.Meal;
import com.diet_planner.backend.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MealRepository extends JpaRepository<Meal, UUID> {

    List<Meal> findByMealType(MealType mealType);
}