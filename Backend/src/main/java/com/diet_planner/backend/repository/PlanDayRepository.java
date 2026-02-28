package com.diet_planner.backend.repository;

import com.diet_planner.backend.entity.PlanDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanDayRepository extends JpaRepository<PlanDay, UUID> {
}