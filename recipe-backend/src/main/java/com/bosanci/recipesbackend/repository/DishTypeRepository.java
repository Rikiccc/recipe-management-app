package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.DishType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishTypeRepository extends JpaRepository<DishType, Long> {
}