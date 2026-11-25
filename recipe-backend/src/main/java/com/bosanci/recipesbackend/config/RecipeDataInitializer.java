package com.bosanci.recipesbackend.config;

import com.bosanci.recipesbackend.model.*;
import com.bosanci.recipesbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class RecipeDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(RecipeDataInitializer.class);

    private final RecipeRepository recipeRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MealTypeRepository mealTypeRepository;
    private final DishTypeRepository dishTypeRepository;
    private final DietTypeRepository dietTypeRepository;
    private final AllergenRepository allergenRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seed() {
        if (recipeRepository.count() > 0) {
            return;
        }

        User admin = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .findFirst()
                .orElse(null);
        if (admin == null) {
            log.warn("Nema administratora u bazi, test recepti nisu dodati.");
            return;
        }
        User ana = createUser("ana", "ana@recipes.com", "Vojvodina");
        User marko = createUser("marko", "marko@recipes.com", "Dalmacija");

        Map<String, MealType> meals = byName(mealTypeRepository.findAll(), MealType::getName);
        Map<String, DishType> dishes = byName(dishTypeRepository.findAll(), DishType::getName);
        Map<String, DietType> diets = byName(dietTypeRepository.findAll(), DietType::getName);
        Map<String, Allergen> allergens = byName(allergenRepository.findAll(), Allergen::getName);

        LocalDateTime now = LocalDateTime.now();

        Recipe pancakes = recipe("Palačinke sa džemom", "Klasične tanke palačinke za doručak.", ana,
                meals.get("Doručak"), dishes.get("Palačinke"), diets.get("Vegetarijanska"), 4, 30, now.minusDays(2),
                allergens, "Mleko", "Gluten", "Jaja");
        Recipe chickenSoup = recipe("Domaća pileća supa", "Bistra supa sa domaćim rezancima.", marko,
                meals.get("Ručak"), dishes.get("Supa"), diets.get("Mrsna"), 6, 90, now.minusDays(5),
                allergens, "Gluten", "Jaja");
        Recipe risotto = recipe("Rižoto sa pečurkama", "Kremasti rižoto sa šampinjonima i parmezanom.", ana,
                meals.get("Večera"), dishes.get("Rižoto"), diets.get("Vegetarijanska"), 4, 45, now.minusDays(1),
                allergens, "Mleko");
        Recipe beanStew = recipe("Posni pasulj", "Gust pasulj sa paprikom i lukom.", admin,
                meals.get("Ručak"), dishes.get("Čorba"), diets.get("Posna"), 6, 120, now.minusDays(12),
                allergens);
        Recipe tuna = recipe("Tuna sa grilovanim povrćem", "Lagano jelo od ribe sa sezonskim povrćem.", marko,
                meals.get("Glavna jela"), dishes.get("Jelo od ribe"), diets.get("Pesketarijanska"), 2, 35,
                now.minusDays(8), allergens, "Riba");
        Recipe veganBurger = recipe("Veganski burger od leblebija", "Burger od leblebija sa avokadom.", ana,
                meals.get("Glavna jela"), dishes.get("Burger"), diets.get("Veganska"), 4, 40, now.minusDays(20),
                allergens, "Gluten", "Soja");
        Recipe pizza = recipe("Pica margarita", "Tanko testo, paradajz sos i mocarela.", marko,
                meals.get("Večera"), dishes.get("Pica"), diets.get("Vegetarijanska"), 2, 75, now.minusDays(3),
                allergens, "Mleko", "Gluten");
        Recipe chocolateCake = recipe("Čokoladna torta", "Sočna torta sa čokoladnim filom.", admin,
                meals.get("Deserti"), dishes.get("Torta"), diets.get("Mrsna"), 12, 150, now.minusDays(40),
                allergens, "Mleko", "Gluten", "Jaja", "Orašasti plodovi");
        Recipe greekSalad = recipe("Grčka salata", "Paradajz, krastavac, masline i feta sir.", ana,
                meals.get("Salate"), dishes.get("Jelo od povrća"), diets.get("Vegetarijanska"), 2, 15,
                now.minusHours(6), allergens, "Mleko");
        Recipe sandwich = recipe("Sendvič sa piletinom", "Brz sendvič za užinu.", marko,
                meals.get("Doručak"), dishes.get("Sendvič"), diets.get("Hrono"), 1, 10, now.minusDays(15),
                allergens, "Gluten");

        recipeRepository.saveAll(List.of(pancakes, chickenSoup, risotto, beanStew, tuna, veganBurger, pizza,
                chocolateCake, greekSalad, sandwich));

        // Ocene iz poslednjih mesec dana odredjuju popularnost.
        rate(risotto, admin, 5, now.minusDays(1));
        rate(risotto, marko, 5, now.minusHours(20));
        rate(pancakes, admin, 5, now.minusDays(2));
        rate(pancakes, marko, 4, now.minusDays(1));
        rate(chickenSoup, ana, 4, now.minusDays(4));
        rate(chickenSoup, admin, 5, now.minusDays(3));
        rate(pizza, ana, 4, now.minusDays(2));
        rate(tuna, admin, 3, now.minusDays(6));
        rate(beanStew, ana, 3, now.minusDays(10));
        rate(veganBurger, marko, 2, now.minusDays(18));
        // Old rating (more than a month ago), cake
        rate(chocolateCake, ana, 5, now.minusDays(38));

        log.info("Dodati test recepti i ocene za pocetnu stranicu.");
    }

    private User createUser(String username, String email, String region) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseGet(() -> {
                    User user = new User();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode("test1234"));
                    user.setRole(Role.USER);
                    user.setEnabled(true);
                    user.setRegion(region);
                    return userRepository.save(user);
                });
    }

    private Recipe recipe(String name, String description, User author, MealType mealType, DishType dishType,
                          DietType dietType, int servings, int minutes, LocalDateTime createdAt,
                          Map<String, Allergen> allAllergens, String... allergenNames) {
        return Recipe.builder()
                .name(name)
                .description(description)
                .author(author)
                .mealType(mealType)
                .dishType(dishType)
                .dietType(dietType)
                .servings(servings)
                .preparationTimeMinutes(minutes)
                .createdAt(createdAt)
                .allergens(Arrays.stream(allergenNames)
                        .map(allAllergens::get)
                        .filter(java.util.Objects::nonNull)
                        .collect(Collectors.toCollection(HashSet::new)))
                .build();
    }

    private void rate(Recipe recipe, User user, int score, LocalDateTime createdAt) {
        ratingRepository.save(Rating.builder()
                .recipe(recipe)
                .user(user)
                .score(score)
                .createdAt(createdAt)
                .build());
    }

    private static <T> Map<String, T> byName(List<T> items, Function<T, String> name) {
        return items.stream().collect(Collectors.toMap(name, Function.identity()));
    }
}
