package com.bosanci.recipesbackend.controller;

import com.bosanci.recipesbackend.dto.LookupItemDto;
import com.bosanci.recipesbackend.service.LookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints for reading lookup tables. Used by recipe formulars,
 * search filters, and admin part of the app.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LookupController {

    private final LookupService lookupService;

    @GetMapping("/meal-types")
    public List<LookupItemDto> getMealTypes() {
        return lookupService.getMealTypes();
    }

    @GetMapping("/meal-types/{id}")
    public LookupItemDto getMealType(@PathVariable Long id) {
        return lookupService.getMealType(id);
    }

    @GetMapping("/dish-types")
    public List<LookupItemDto> getDishTypes() {
        return lookupService.getDishTypes();
    }

    @GetMapping("/dish-types/{id}")
    public LookupItemDto getDishType(@PathVariable Long id) {
        return lookupService.getDishType(id);
    }

    @GetMapping("/diet-types")
    public List<LookupItemDto> getDietTypes() {
        return lookupService.getDietTypes();
    }

    @GetMapping("/diet-types/{id}")
    public LookupItemDto getDietType(@PathVariable Long id) {
        return lookupService.getDietType(id);
    }

    @GetMapping("/ingredient-categories")
    public List<LookupItemDto> getIngredientCategories() {
        return lookupService.getIngredientCategories();
    }

    @GetMapping("/ingredient-categories/{id}")
    public LookupItemDto getIngredientCategory(@PathVariable Long id) {
        return lookupService.getIngredientCategory(id);
    }

    @GetMapping("/allergens")
    public List<LookupItemDto> getAllergens() {
        return lookupService.getAllergens();
    }

    @GetMapping("/allergens/{id}")
    public LookupItemDto getAllergen(@PathVariable Long id) {
        return lookupService.getAllergen(id);
    }
}
