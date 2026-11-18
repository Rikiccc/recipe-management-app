package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.dto.LookupItemDto;
import com.bosanci.recipesbackend.exception.ResourceNotFoundException;
import com.bosanci.recipesbackend.model.Allergen;
import com.bosanci.recipesbackend.model.DietType;
import com.bosanci.recipesbackend.model.DishType;
import com.bosanci.recipesbackend.model.IngredientCategory;
import com.bosanci.recipesbackend.model.MealType;
import com.bosanci.recipesbackend.repository.AllergenRepository;
import com.bosanci.recipesbackend.repository.DietTypeRepository;
import com.bosanci.recipesbackend.repository.DishTypeRepository;
import com.bosanci.recipesbackend.repository.IngredientCategoryRepository;
import com.bosanci.recipesbackend.repository.MealTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

// reading lookup tables

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LookupService {

    private static final Sort BY_NAME = Sort.by("name").ascending();

    private final MealTypeRepository mealTypeRepository;
    private final DishTypeRepository dishTypeRepository;
    private final DietTypeRepository dietTypeRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final AllergenRepository allergenRepository;

    public List<LookupItemDto> getMealTypes() {
        return findAll(mealTypeRepository, m -> new LookupItemDto(m.getId(), m.getName()));
    }

    public LookupItemDto getMealType(Long id) {
        return findById(mealTypeRepository, id, "Tip obroka", m -> new LookupItemDto(m.getId(), m.getName()));
    }

    public List<LookupItemDto> getDishTypes() {
        return findAll(dishTypeRepository, d -> new LookupItemDto(d.getId(), d.getName()));
    }

    public LookupItemDto getDishType(Long id) {
        return findById(dishTypeRepository, id, "Tip jela", d -> new LookupItemDto(d.getId(), d.getName()));
    }

    public List<LookupItemDto> getDietTypes() {
        return findAll(dietTypeRepository, d -> new LookupItemDto(d.getId(), d.getName()));
    }

    public LookupItemDto getDietType(Long id) {
        return findById(dietTypeRepository, id, "Tip ishrane", d -> new LookupItemDto(d.getId(), d.getName()));
    }

    public List<LookupItemDto> getIngredientCategories() {
        return findAll(ingredientCategoryRepository, c -> new LookupItemDto(c.getId(), c.getName()));
    }

    public LookupItemDto getIngredientCategory(Long id) {
        return findById(ingredientCategoryRepository, id, "Kategorija namirnica",
                c -> new LookupItemDto(c.getId(), c.getName()));
    }

    public List<LookupItemDto> getAllergens() {
        return findAll(allergenRepository, a -> new LookupItemDto(a.getId(), a.getName()));
    }

    public LookupItemDto getAllergen(Long id) {
        return findById(allergenRepository, id, "Alergen", a -> new LookupItemDto(a.getId(), a.getName()));
    }

    private <T> List<LookupItemDto> findAll(JpaRepository<T, Long> repository, Function<T, LookupItemDto> mapper) {
        return repository.findAll(BY_NAME).stream().map(mapper).toList();
    }

    private <T> LookupItemDto findById(JpaRepository<T, Long> repository, Long id, String resourceName,
                                       Function<T, LookupItemDto> mapper) {
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
    }
}
