/** Lookup table item sent by backend (LookupItemDto). */

export interface LookupItem {
  id: number;
  name: string;
}

/** Meal tyoe (dorucak, rucak, vecera...). */

export type MealType = LookupItem;

/** Dish type (burger, pica, torta...). */
export type DishType = LookupItem;

/** Diet type (vegetarijanska, posna...). */
export type DietType = LookupItem;

/** Ingredient category (povrce, voce, meso...). */
export type IngredientCategory = LookupItem;

/** Allergens (gluten, mleko, kikiriki...). */
export type Allergen = LookupItem;

/** All lookup tables, for search filters and recipe forms. */
export interface LookupData {
  mealTypes: MealType[];
  dishTypes: DishType[];
  dietTypes: DietType[];
  ingredientCategories: IngredientCategory[];
  allergens: Allergen[];
}
