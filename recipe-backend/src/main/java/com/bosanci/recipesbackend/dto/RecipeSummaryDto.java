package com.bosanci.recipesbackend.dto;

import java.time.LocalDateTime;
import java.util.List;


public record RecipeSummaryDto(
        Long id,
        String name,
        Long authorId,
        String authorUsername,
        String mealType,
        String dishType,
        String dietType,
        List<String> allergens,
        Integer servings,
        Integer preparationTimeMinutes,
        String mainImageUrl,
        Double averageRating,
        long ratingCount,
        LocalDateTime createdAt
) {
}
