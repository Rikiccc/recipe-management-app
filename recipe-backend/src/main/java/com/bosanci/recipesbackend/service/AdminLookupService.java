package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.dto.LookupItemDto;
import com.bosanci.recipesbackend.dto.LookupRequestDto;
import com.bosanci.recipesbackend.exception.BadRequestException;
import com.bosanci.recipesbackend.exception.ResourceNotFoundException;
import com.bosanci.recipesbackend.model.*;
import com.bosanci.recipesbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLookupService {

    private final MealTypeRepository mealTypeRepository;
    private final DishTypeRepository dishTypeRepository;
    private final DietTypeRepository dietTypeRepository;
    private final IngredientCategoryRepository categoryRepository;
    private final AllergenRepository allergenRepository;

    // --- MEAL TYPES ---
    public LookupItemDto createMealType(LookupRequestDto request) {
        String name = request.getName().trim();
        if (mealTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Tip obroka sa tim nazivom već postoji.");
        }
        MealType saved = mealTypeRepository.save(new MealType(null, name));
        return new LookupItemDto(saved.getId(), saved.getName());
    }

    public LookupItemDto updateMealType(Long id, LookupRequestDto request) {
        MealType entity = mealTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tip obroka nije pronađen."));
        String name = request.getName().trim();
        if (!entity.getName().equalsIgnoreCase(name) && mealTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Tip obroka sa tim nazivom već postoji.");
        }
        entity.setName(name);
        MealType updated = mealTypeRepository.save(entity);
        return new LookupItemDto(updated.getId(), updated.getName());
    }

    public void deleteMealType(Long id) {
        if (!mealTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tip obroka nije pronađen.");
        }
        mealTypeRepository.deleteById(id);
    }

    // --- DISH TYPES ---
    public LookupItemDto createDishType(LookupRequestDto request) {
        String name = request.getName().trim();
        if (dishTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Tip jela sa tim nazivom već postoji.");
        }
        DishType saved = dishTypeRepository.save(new DishType(null, name));
        return new LookupItemDto(saved.getId(), saved.getName());
    }

    public LookupItemDto updateDishType(Long id, LookupRequestDto request) {
        DishType entity = dishTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tip jela nije pronađen."));
        String name = request.getName().trim();
        if (!entity.getName().equalsIgnoreCase(name) && dishTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Tip jela sa tim nazivom već postoji.");
        }
        entity.setName(name);
        DishType updated = dishTypeRepository.save(entity);
        return new LookupItemDto(updated.getId(), updated.getName());
    }

    public void deleteDishType(Long id) {
        if (!dishTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tip jela nije pronađen.");
        }
        dishTypeRepository.deleteById(id);
    }

    // --- DIET TYPES ---
    public LookupItemDto createDietType(LookupRequestDto request) {
        String name = request.getName().trim();
        if (dietTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Tip ishrane sa tim nazivom već postoji.");
        }
        DietType saved = dietTypeRepository.save(new DietType(null, name));
        return new LookupItemDto(saved.getId(), saved.getName());
    }

    public LookupItemDto updateDietType(Long id, LookupRequestDto request) {
        DietType entity = dietTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tip ishrane nije pronađen."));
        String name = request.getName().trim();
        if (!entity.getName().equalsIgnoreCase(name) && dietTypeRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Tip ishrane sa tim nazivom već postoji.");
        }
        entity.setName(name);
        DietType updated = dietTypeRepository.save(entity);
        return new LookupItemDto(updated.getId(), updated.getName());
    }

    public void deleteDietType(Long id) {
        if (!dietTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tip ishrane nije pronađen.");
        }
        dietTypeRepository.deleteById(id);
    }

    // --- INGREDIENT CATEGORIES ---
    public LookupItemDto createIngredientCategory(LookupRequestDto request) {
        String name = request.getName().trim();
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Kategorija namirnica sa tim nazivom već postoji.");
        }
        IngredientCategory saved = categoryRepository.save(new IngredientCategory(null, name));
        return new LookupItemDto(saved.getId(), saved.getName());
    }

    public LookupItemDto updateIngredientCategory(Long id, LookupRequestDto request) {
        IngredientCategory entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorija namirnica nije pronađena."));
        String name = request.getName().trim();
        if (!entity.getName().equalsIgnoreCase(name) && categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Kategorija namirnica sa tim nazivom već postoji.");
        }
        entity.setName(name);
        IngredientCategory updated = categoryRepository.save(entity);
        return new LookupItemDto(updated.getId(), updated.getName());
    }

    public void deleteIngredientCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kategorija namirnica nije pronađena.");
        }
        categoryRepository.deleteById(id);
    }

    // --- ALLERGENS ---
    public LookupItemDto createAllergen(LookupRequestDto request) {
        String name = request.getName().trim();
        if (allergenRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Alergen sa tim nazivom već postoji.");
        }
        Allergen saved = allergenRepository.save(new Allergen(null, name));
        return new LookupItemDto(saved.getId(), saved.getName());
    }

    public LookupItemDto updateAllergen(Long id, LookupRequestDto request) {
        Allergen entity = allergenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alergen nije pronađen."));
        String name = request.getName().trim();
        if (!entity.getName().equalsIgnoreCase(name) && allergenRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Alergen sa tim nazivom već postoji.");
        }
        entity.setName(name);
        Allergen updated = allergenRepository.save(entity);
        return new LookupItemDto(updated.getId(), updated.getName());
    }

    public void deleteAllergen(Long id) {
        if (!allergenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alergen nije pronađen.");
        }
        allergenRepository.deleteById(id);
    }
}