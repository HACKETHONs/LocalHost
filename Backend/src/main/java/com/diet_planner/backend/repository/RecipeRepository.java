package com.diet_planner.backend.repository;

import com.diet_planner.backend.entity.Recipe;
import com.diet_planner.backend.entity.RecipeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    List<Recipe> findByType(RecipeType type);

    List<Recipe> findByRequiresApplianceFalse();

    List<Recipe> findByTypeAndRequiresApplianceFalse(RecipeType type);
}