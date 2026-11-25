package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergenRepository extends JpaRepository<Allergen, Long> {
}