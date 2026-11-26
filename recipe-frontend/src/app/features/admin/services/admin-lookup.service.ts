import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface LookupItem {
  id: number;
  name: string;
}

export type LookupCategoryKey =
  | 'meal-types'
  | 'dish-types'
  | 'diet-types'
  | 'ingredient-categories'
  | 'allergens';

@Injectable({
  providedIn: 'root'
})
export class AdminLookupService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  getAll(category: LookupCategoryKey): Observable<LookupItem[]> {
    return this.http.get<LookupItem[]>(`${this.baseUrl}/${category}`);
  }

  // Authorization header (Bearer JWT) is added by authInterceptor.
  create(category: LookupCategoryKey, name: string): Observable<LookupItem> {
    return this.http.post<LookupItem>(`${this.baseUrl}/admin/${category}`, { name });
  }

  update(category: LookupCategoryKey, id: number, name: string): Observable<LookupItem> {
    return this.http.put<LookupItem>(`${this.baseUrl}/admin/${category}/${id}`, { name });
  }

  delete(category: LookupCategoryKey, id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/${category}/${id}`);
  }
}