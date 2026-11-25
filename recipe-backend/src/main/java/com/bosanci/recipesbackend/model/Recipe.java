package com.bosanci.recipesbackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Recipe, central entity of the app
 * NOTE: This is minimal version needed for home page (S2-7)
 * It will be updated for functionality (3)
 * CRUD of recipes, ingredients, preparation steps, added photos, videos etc
 */
@Entity
@Table(name = "recipes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_type_id")
    private MealType mealType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_type_id")
    private DishType dishType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_type_id")
    private DietType dietType;

    /**
     * Allerges of recipe, when ingredients are added
     * they are calculted based on allerges of selected ingredients
     */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_allergens",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    @Builder.Default
    private Set<Allergen> allergens = new HashSet<>();

    @Column(nullable = false)
    private Integer servings;


    @Column(name = "preparation_time_minutes", nullable = false)
    private Integer preparationTimeMinutes;


    @Column(name = "main_image_url")
    private String mainImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
