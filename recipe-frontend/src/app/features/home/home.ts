import { Component, afterNextRender, inject, signal } from '@angular/core';
import { HealthStatus, LookupData, LookupItem } from '../../core/models';
import { HealthService } from '../../core/services/health.service';
import { LookupService } from '../../core/services/lookup.service';
import { toErrorMessage } from '../../core/services/api-error.util';

interface LookupGroup {
  title: string;
  icon: string;
  items: LookupItem[];
}

/**
 * Home page. Checks backend connection and shows
 * lookup tables from database
 */
@Component({
  selector: 'app-home',
  standalone: false,
  styleUrl: './home.scss',
  templateUrl: './home.html',
})
export class Home {
  private readonly healthService = inject(HealthService);
  private readonly lookupService = inject(LookupService);

  readonly health = signal<HealthStatus | null>(null);
  readonly groups = signal<LookupGroup[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    // Data loaded in browser only
    afterNextRender(() => this.load());
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);

    this.healthService.check().subscribe({
      next: (status) => this.health.set(status),
      error: () => this.health.set(null),
    });

    this.lookupService.getAll().subscribe({
      next: (data) => {
        this.groups.set(this.toGroups(data));
        this.loading.set(false);
      },
      error: (err: unknown) => {
        this.lookupService.clearCache();
        this.error.set(toErrorMessage(err));
        this.loading.set(false);
      },
    });
  }

  private toGroups(data: LookupData): LookupGroup[] {
    return [
      { title: 'Tipovi obroka', icon: 'schedule', items: data.mealTypes },
      { title: 'Tipovi jela', icon: 'restaurant_menu', items: data.dishTypes },
      { title: 'Tipovi ishrane', icon: 'eco', items: data.dietTypes },
      { title: 'Kategorije namirnica', icon: 'category', items: data.ingredientCategories },
      { title: 'Alergeni', icon: 'warning', items: data.allergens },
    ];
  }
}
