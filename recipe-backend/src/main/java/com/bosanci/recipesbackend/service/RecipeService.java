package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.dto.PageResponse;
import com.bosanci.recipesbackend.dto.RecipeSummaryDto;
import com.bosanci.recipesbackend.exception.BadRequestException;
import com.bosanci.recipesbackend.model.Allergen;
import com.bosanci.recipesbackend.model.Recipe;
import com.bosanci.recipesbackend.repository.RatingRepository;
import com.bosanci.recipesbackend.repository.RatingRepository.RatingSummary;
import com.bosanci.recipesbackend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {


    private static final int POPULARITY_PERIOD_DAYS = 30;
    private static final int MAX_PAGE_SIZE = 50;

    private final RecipeRepository recipeRepository;
    private final RatingRepository ratingRepository;

    public PageResponse<RecipeSummaryDto> getNewest(int page, int size) {
        PageRequest request = pageRequest(page, size,
                Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
        return toPageResponse(recipeRepository.findAllBy(request));
    }

    public PageResponse<RecipeSummaryDto> getPopular(int page, int size) {
        LocalDateTime since = LocalDateTime.now().minusDays(POPULARITY_PERIOD_DAYS);
        return toPageResponse(recipeRepository.findPopularSince(since, pageRequest(page, size, Sort.unsorted())));
    }

    private PageRequest pageRequest(int page, int size, Sort sort) {
        if (page < 0) {
            throw new BadRequestException("Broj stranice ne moze biti negativan.");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new BadRequestException("Velicina stranice mora biti izmedju 1 i " + MAX_PAGE_SIZE + ".");
        }
        return PageRequest.of(page, size, sort);
    }

    private PageResponse<RecipeSummaryDto> toPageResponse(Page<Recipe> recipes) {
        return PageResponse.of(recipes, this::toSummaries);
    }

    private List<RecipeSummaryDto> toSummaries(List<Recipe> recipes) {
        if (recipes.isEmpty()) {
            return List.of();
        }
        List<Long> ids = recipes.stream().map(Recipe::getId).toList();
        Map<Long, RatingSummary> ratings = ratingRepository.summarize(ids).stream()
                .collect(Collectors.toMap(RatingSummary::getRecipeId, Function.identity()));
        return recipes.stream().map(recipe -> toSummary(recipe, ratings.get(recipe.getId()))).toList();
    }

    private RecipeSummaryDto toSummary(Recipe recipe, RatingSummary rating) {
        List<String> allergens = recipe.getAllergens().stream()
                .map(Allergen::getName)
                .sorted(Comparator.naturalOrder())
                .toList();
        Double average = rating == null ? null : Math.round(rating.getAverageScore() * 10.0) / 10.0;
        long count = rating == null ? 0 : rating.getRatingCount();

        return new RecipeSummaryDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getAuthor().getId(),
                recipe.getAuthor().getUsername(),
                recipe.getMealType() != null ? recipe.getMealType().getName() : null,
                recipe.getDishType() != null ? recipe.getDishType().getName() : null,
                recipe.getDietType() != null ? recipe.getDietType().getName() : null,
                allergens,
                recipe.getServings(),
                recipe.getPreparationTimeMinutes(),
                recipe.getMainImageUrl(),
                average,
                count,
                recipe.getCreatedAt()
        );
    }
}
