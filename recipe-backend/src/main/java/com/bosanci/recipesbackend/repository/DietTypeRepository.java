package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.DietType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietTypeRepository extends JpaRepository<DietType, Long> {
}