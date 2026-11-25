import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page, RecipeSummary } from '../models';


@Injectable({ providedIn: 'root' })
export class RecipeService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/recipes`;


  getNewest(page = 0, size = 12): Observable<Page<RecipeSummary>> {
    return this.http.get<Page<RecipeSummary>>(`${this.baseUrl}/newest`, {
      params: this.pageParams(page, size),
    });
  }


  getPopular(page = 0, size = 12): Observable<Page<RecipeSummary>> {
    return this.http.get<Page<RecipeSummary>>(`${this.baseUrl}/popular`, {
      params: this.pageParams(page, size),
    });
  }

  private pageParams(page: number, size: number): HttpParams {
    return new HttpParams().set('page', page).set('size', size);
  }
}
