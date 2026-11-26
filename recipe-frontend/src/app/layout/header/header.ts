import { Component, afterNextRender, computed, inject, signal } from '@angular/core';
import { AuthService } from '../../core/auth';

@Component({
  selector: 'app-header',
  standalone: false,
  styleUrl: './header.scss',
  templateUrl: './header.html',
})
export class Header {
  protected readonly auth = inject(AuthService);

  protected readonly ready = signal(false);

  protected readonly initial = computed(() => (this.auth.user()?.username ?? '?').charAt(0).toUpperCase());

  constructor() {
    afterNextRender(() => this.ready.set(true));
  }

  protected logout(): void {
    this.auth.logout();
  }
}