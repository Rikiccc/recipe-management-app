/** RecipeSummaryDto. */
export interface RecipeSummary {
  id: number;
  name: string;
  authorId: number;
  authorUsername: string;
  mealType: string | null;
  dishType: string | null;
  dietType: string | null;
  allergens: string[];
  servings: number;
  preparationTimeMinutes: number;
  mainImageUrl: string | null;
  averageRating: number | null;
  ratingCount: number;
  createdAt: string;
}
