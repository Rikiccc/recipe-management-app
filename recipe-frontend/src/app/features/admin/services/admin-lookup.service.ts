import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

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
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private getAdminHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    if (token) {
      return new HttpHeaders({ Authorization: `Bearer ${token}` });
    }
    // Fallback za lokalno testiranje dok JWT login nije spojen (admin:admin123)
    const basicAuth = btoa('admin:admin123');
    return new HttpHeaders({ Authorization: `Basic ${basicAuth}` });
  }

  getAll(category: LookupCategoryKey): Observable<LookupItem[]> {
    return this.http.get<LookupItem[]>(`${this.baseUrl}/lookup/${category}`);
  }

  create(category: LookupCategoryKey, name: string): Observable<LookupItem> {
    return this.http.post<LookupItem>(
      `${this.baseUrl}/admin/${category}`,
      { name },
      { headers: this.getAdminHeaders() }
    );
  }

  update(category: LookupCategoryKey, id: number, name: string): Observable<LookupItem> {
    return this.http.put<LookupItem>(
      `${this.baseUrl}/admin/${category}/${id}`,
      { name },
      { headers: this.getAdminHeaders() }
    );
  }

  delete(category: LookupCategoryKey, id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/admin/${category}/${id}`,
      { headers: this.getAdminHeaders() }
    );
  }
}