package com.bosanci.recipesbackend.repository;

import com.bosanci.recipesbackend.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    /** Average rating and number of ratings together */
    @Query("""
            select rt.recipe.id as recipeId, avg(rt.score) as averageScore, count(rt) as ratingCount
            from Rating rt
            where rt.recipe.id in :recipeIds
            group by rt.recipe.id
            """)
    List<RatingSummary> summarize(@Param("recipeIds") Collection<Long> recipeIds);

    interface RatingSummary {
        Long getRecipeId();

        Double getAverageScore();

        Long getRatingCount();
    }
}
