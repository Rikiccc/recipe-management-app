package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealTypeRepository extends JpaRepository<MealType, Long> {
    boolean existsByNameIgnoreCase(String name);
}