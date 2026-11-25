package com.bosanci.recipesbackend.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * Result page in stable JSON format
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <E, T> PageResponse<T> of(Page<E> page, Function<List<E>, List<T>> mapper) {
        return new PageResponse<>(mapper.apply(page.getContent()), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
