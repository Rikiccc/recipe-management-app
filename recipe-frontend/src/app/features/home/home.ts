import { Component, afterNextRender, inject, signal } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Observable } from 'rxjs';
import { Page, RecipeSummary } from '../../core/models';
import { RecipeService } from '../../core/services/recipe.service';
import { toErrorMessage } from '../../core/services/api-error.util';


type RecipeTab = 'popular' | 'newest';
type HomeTab = RecipeTab | 'available';

interface RecipeListState {
  recipes: RecipeSummary[];
  page: number;
  size: number;
  total: number;
  loading: boolean;
  loaded: boolean;
  error: string | null;
}

const PAGE_SIZE = 12;

const initialState = (): RecipeListState => ({
  recipes: [],
  page: 0,
  size: PAGE_SIZE,
  total: 0,
  loading: false,
  loaded: false,
  error: null,
});


@Component({
  selector: 'app-home',
  standalone: false,
  styleUrl: './home.scss',
  templateUrl: './home.html',
})
export class Home {
  private readonly recipeService = inject(RecipeService);

  readonly tabs: HomeTab[] = ['popular', 'newest', 'available'];
  readonly pageSizeOptions = [6, 12, 24];

  readonly selectedIndex = signal(0);
  readonly popular = signal<RecipeListState>(initialState());
  readonly newest = signal<RecipeListState>(initialState());

  constructor() {

    afterNextRender(() => this.load('popular'));
  }

  onTabChange(index: number): void {
    this.selectedIndex.set(index);
    const tab = this.tabs[index];
    if (tab !== 'available' && !this.stateOf(tab)().loaded) {
      this.load(tab);
    }
  }

  onPage(tab: RecipeTab, event: PageEvent): void {
    this.stateOf(tab).update((s) => ({ ...s, page: event.pageIndex, size: event.pageSize }));
    this.load(tab);
  }

  retry(tab: RecipeTab): void {
    this.load(tab);
  }

  load(tab: RecipeTab): void {
    const state = this.stateOf(tab);
    const { page, size } = state();
    state.update((s) => ({ ...s, loading: true, error: null }));

    this.request(tab, page, size).subscribe({
      next: (result) =>
        state.set({
          recipes: result.content,
          page: result.page,
          size: result.size,
          total: result.totalElements,
          loading: false,
          loaded: true,
          error: null,
        }),
      error: (err: unknown) =>
        state.update((s) => ({ ...s, loading: false, loaded: true, error: toErrorMessage(err) })),
    });
  }

  private request(tab: RecipeTab, page: number, size: number): Observable<Page<RecipeSummary>> {
    return tab === 'popular'
      ? this.recipeService.getPopular(page, size)
      : this.recipeService.getNewest(page, size);
  }

  private stateOf(tab: RecipeTab) {
    return tab === 'popular' ? this.popular : this.newest;
  }
}
