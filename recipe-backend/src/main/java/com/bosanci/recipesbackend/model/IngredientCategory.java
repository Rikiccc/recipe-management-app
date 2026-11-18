package com.bosanci.recipesbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredient_categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IngredientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}