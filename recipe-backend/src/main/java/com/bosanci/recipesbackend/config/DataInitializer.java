package com.bosanci.recipesbackend.config;

import com.bosanci.recipesbackend.model.*;
import com.bosanci.recipesbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MealTypeRepository mealTypeRepository;
    private final DishTypeRepository dishTypeRepository;
    private final DietTypeRepository dietTypeRepository;
    private final IngredientCategoryRepository categoryRepository;
    private final AllergenRepository allergenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@recipes.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin.setRegion("Srbija");
            admin.setBiography("Glavni administrator sistema.");
            userRepository.save(admin);
        }

        if (mealTypeRepository.count() == 0) {
            List.of("Doručak", "Ručak", "Večera", "Glavna jela", "Predjela", "Salate", "Deserti", "Pića")
                    .forEach(name -> mealTypeRepository.save(new MealType(null, name)));
        }

        if (dishTypeRepository.count() == 0) {
            List.of("Supa", "Čorba", "Testenina", "Pica", "Sendvič", "Burger", "Rižoto",
                            "Jelo od mesa", "Jelo od ribe", "Jelo od povrća", "Pekarski proizvod", "Kolač", "Torta", "Palačinke")
                    .forEach(name -> dishTypeRepository.save(new DishType(null, name)));
        }

        if (dietTypeRepository.count() == 0) {
            List.of("Vegetarijanska", "Veganska", "Posna", "Mrsna", "Hrono", "Pesketarijanska")
                    .forEach(name -> dietTypeRepository.save(new DietType(null, name)));
        }

        if (categoryRepository.count() == 0) {
            List.of("Povrće", "Voće", "Meso", "Riba", "Mlečni proizvodi", "Žitarice", "Začini", "Orašasti plodovi")
                    .forEach(name -> categoryRepository.save(new IngredientCategory(null, name)));
        }

        if (allergenRepository.count() == 0) {
            List.of("Mleko", "Gluten", "Kikiriki", "Jaja", "Riba", "Orašasti plodovi", "Soja")
                    .forEach(name -> allergenRepository.save(new Allergen(null, name)));
        }
    }
}