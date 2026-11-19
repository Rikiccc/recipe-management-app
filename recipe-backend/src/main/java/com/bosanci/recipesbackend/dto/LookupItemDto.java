package com.bosanci.recipesbackend.dto;

/*
 * Simple DTO for lookup items (dish types, meals, diet,
 * ingredient categories and allergens). All lookups have id and name.
 */
public record LookupItemDto(Long id, String name) {
}
