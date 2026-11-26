package com.bosanci.recipesbackend.controller;

import com.bosanci.recipesbackend.dto.LookupItemDto;
import com.bosanci.recipesbackend.dto.LookupRequestDto;
import com.bosanci.recipesbackend.service.AdminLookupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLookupController {

    private final AdminLookupService adminLookupService;

    // --- MEAL TYPES ---
    @PostMapping("/meal-types")
    public ResponseEntity<LookupItemDto> createMealType(@Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminLookupService.createMealType(request));
    }

    @PutMapping("/meal-types/{id}")
    public ResponseEntity<LookupItemDto> updateMealType(@PathVariable Long id, @Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.ok(adminLookupService.updateMealType(id, request));
    }

    @DeleteMapping("/meal-types/{id}")
    public ResponseEntity<Void> deleteMealType(@PathVariable Long id) {
        adminLookupService.deleteMealType(id);
        return ResponseEntity.noContent().build();
    }

    // --- DISH TYPES ---
    @PostMapping("/dish-types")
    public ResponseEntity<LookupItemDto> createDishType(@Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminLookupService.createDishType(request));
    }

    @PutMapping("/dish-types/{id}")
    public ResponseEntity<LookupItemDto> updateDishType(@PathVariable Long id, @Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.ok(adminLookupService.updateDishType(id, request));
    }

    @DeleteMapping("/dish-types/{id}")
    public ResponseEntity<Void> deleteDishType(@PathVariable Long id) {
        adminLookupService.deleteDishType(id);
        return ResponseEntity.noContent().build();
    }

    // --- DIET TYPES ---
    @PostMapping("/diet-types")
    public ResponseEntity<LookupItemDto> createDietType(@Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminLookupService.createDietType(request));
    }

    @PutMapping("/diet-types/{id}")
    public ResponseEntity<LookupItemDto> updateDietType(@PathVariable Long id, @Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.ok(adminLookupService.updateDietType(id, request));
    }

    @DeleteMapping("/diet-types/{id}")
    public ResponseEntity<Void> deleteDietType(@PathVariable Long id) {
        adminLookupService.deleteDietType(id);
        return ResponseEntity.noContent().build();
    }

    // --- INGREDIENT CATEGORIES ---
    @PostMapping("/ingredient-categories")
    public ResponseEntity<LookupItemDto> createIngredientCategory(@Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminLookupService.createIngredientCategory(request));
    }

    @PutMapping("/ingredient-categories/{id}")
    public ResponseEntity<LookupItemDto> updateIngredientCategory(@PathVariable Long id, @Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.ok(adminLookupService.updateIngredientCategory(id, request));
    }

    @DeleteMapping("/ingredient-categories/{id}")
    public ResponseEntity<Void> deleteIngredientCategory(@PathVariable Long id) {
        adminLookupService.deleteIngredientCategory(id);
        return ResponseEntity.noContent().build();
    }

    // --- ALLERGENS ---
    @PostMapping("/allergens")
    public ResponseEntity<LookupItemDto> createAllergen(@Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminLookupService.createAllergen(request));
    }

    @PutMapping("/allergens/{id}")
    public ResponseEntity<LookupItemDto> updateAllergen(@PathVariable Long id, @Valid @RequestBody LookupRequestDto request) {
        return ResponseEntity.ok(adminLookupService.updateAllergen(id, request));
    }

    @DeleteMapping("/allergens/{id}")
    public ResponseEntity<Void> deleteAllergen(@PathVariable Long id) {
        adminLookupService.deleteAllergen(id);
        return ResponseEntity.noContent().build();
    }
}