package com.bosanci.recipesbackend.controller;

import com.bosanci.recipesbackend.model.MealType;
import com.bosanci.recipesbackend.repository.MealTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    private final MealTypeRepository mealTypeRepository;

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "OK", "message", "Backend za recepte radi!");
    }

    @GetMapping("/meal-types")
    public List<MealType> getAllMealTypes() {
        return mealTypeRepository.findAll();
    }
}