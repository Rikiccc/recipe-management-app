package com.bosanci.recipesbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diet_types")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DietType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}