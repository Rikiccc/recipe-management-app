package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /** Newest recipes, sorted by Pageable. */
    @EntityGraph(attributePaths = {"author", "mealType", "dishType", "dietType"})
    Page<Recipe> findAllBy(Pageable pageable);

    /**
     * 
     * Popular recipes: highest avg rating at the moment (last month)
     * Recipes without rating are last
     */
    @Query(value = """
            select r from Recipe r
            left join Rating rt on rt.recipe = r and rt.createdAt >= :since
            group by r
            order by coalesce(avg(rt.score), 0) desc, count(rt) desc, r.createdAt desc, r.id desc
            """,
            countQuery = "select count(r) from Recipe r")
    Page<Recipe> findPopularSince(@Param("since") LocalDateTime since, Pageable pageable);
}
