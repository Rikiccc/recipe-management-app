import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, forkJoin, shareReplay } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Allergen,
  DietType,
  DishType,
  IngredientCategory,
  LookupData,
  MealType,
} from '../models';

/**
 * Reading lookup table from backend. Results is caches, lookup tables
 * are rarely changed. Used by search, recipe form and admin.
 */
@Injectable({ providedIn: 'root' })
export class LookupService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  private allCache$?: Observable<LookupData>;

  getMealTypes(): Observable<MealType[]> {
    return this.http.get<MealType[]>(`${this.baseUrl}/meal-types`);
  }

  getDishTypes(): Observable<DishType[]> {
    return this.http.get<DishType[]>(`${this.baseUrl}/dish-types`);
  }

  getDietTypes(): Observable<DietType[]> {
    return this.http.get<DietType[]>(`${this.baseUrl}/diet-types`);
  }

  getIngredientCategories(): Observable<IngredientCategory[]> {
    return this.http.get<IngredientCategory[]>(`${this.baseUrl}/ingredient-categories`);
  }

  getAllergens(): Observable<Allergen[]> {
    return this.http.get<Allergen[]>(`${this.baseUrl}/allergens`);
  }

  /** All lookup tables  */
  getAll(): Observable<LookupData> {
    this.allCache$ ??= forkJoin({
      mealTypes: this.getMealTypes(),
      dishTypes: this.getDishTypes(),
      dietTypes: this.getDietTypes(),
      ingredientCategories: this.getIngredientCategories(),
      allergens: this.getAllergens(),
    }).pipe(shareReplay({ bufferSize: 1, refCount: false }));
    return this.allCache$;
  }

  /** Deletes cache, ie. admin changes lookup table */
  clearCache(): void {
    this.allCache$ = undefined;
  }
}
