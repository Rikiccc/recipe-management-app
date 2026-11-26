package com.bosanci.recipesbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private boolean enabled = false;

    private String profileImage;
    private String region;

    @Column(length = 1000)
    private String biography;

    @Column(name = "token_version")
    private Integer tokenVersion = 0;

    public int getTokenVersion() {
        return tokenVersion == null ? 0 : tokenVersion;
    }

    public void incrementTokenVersion() {
        tokenVersion = getTokenVersion() + 1;
    }
}