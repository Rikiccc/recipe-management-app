package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
    boolean existsByNameIgnoreCase(String name);
}