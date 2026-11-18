package com.bosanci.recipesbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meal_types")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MealType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}