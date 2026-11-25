import { Component, computed, input } from '@angular/core';
import { RecipeSummary } from '../../../core/models';


@Component({
  selector: 'app-recipe-card',
  standalone: false,
  templateUrl: './recipe-card.html',
  styleUrl: './recipe-card.scss',
})
export class RecipeCard {
  readonly recipe = input.required<RecipeSummary>();


  readonly stars = computed(() => {
    const rating = this.recipe().averageRating ?? 0;
    return [1, 2, 3, 4, 5].map((i) => {
      if (rating >= i) {
        return 'star';
      }
      if (rating >= i - 0.5) {
        return 'star_half';
      }
      return 'star_border';
    });
  });

  readonly tags = computed(() => {
    const r = this.recipe();
    return [r.mealType, r.dishType, r.dietType].filter((tag): tag is string => !!tag);
  });

  readonly preparationTime = computed(() => {
    const minutes = this.recipe().preparationTimeMinutes;
    if (minutes < 60) {
      return `${minutes} min`;
    }
    const hours = Math.floor(minutes / 60);
    const rest = minutes % 60;
    return rest === 0 ? `${hours} h` : `${hours} h ${rest} min`;
  });
}
