package com.bosanci.recipesbackend.controller;

import com.bosanci.recipesbackend.dto.PageResponse;
import com.bosanci.recipesbackend.dto.RecipeSummaryDto;
import com.bosanci.recipesbackend.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/newest")
    public PageResponse<RecipeSummaryDto> getNewest(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "12") int size) {
        return recipeService.getNewest(page, size);
    }

    @GetMapping("/popular")
    public PageResponse<RecipeSummaryDto> getPopular(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "12") int size) {
        return recipeService.getPopular(page, size);
    }
}
