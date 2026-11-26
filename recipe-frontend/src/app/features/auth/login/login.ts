import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, controlError } from '../../../core/auth';
import { applyFieldErrors, toErrorMessage } from '../../../core/services/api-error.util';

@Component({
  selector: 'app-login',
  standalone: false,
  styleUrl: '../auth-forms.scss',
  templateUrl: './login.html',
})
export class Login {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly errorOf = controlError;

  readonly form = this.fb.group({
    usernameOrEmail: ['', [Validators.required, Validators.maxLength(254)]],
    password: ['', [Validators.required, Validators.maxLength(200)]],
  });

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  /** True when login failed because the account is not activated yet. */
  readonly notActivated = signal(false);
  readonly hidePassword = signal(true);
  readonly sessionExpired = this.route.snapshot.queryParamMap.get('reason') === 'expired';

  submit(): void {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    this.notActivated.set(false);

    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => void this.router.navigateByUrl(this.returnUrl()),
      error: (err: unknown) => {
        this.loading.set(false);
        this.notActivated.set(err instanceof HttpErrorResponse && err.status === 403);
        if (!applyFieldErrors(this.form, err)) {
          this.error.set(toErrorMessage(err));
        }
      },
    });
  }

  /** Only paths inside this app are accepted, so a crafted link can't send the user to another site. */
  private returnUrl(): string {
    const value = this.route.snapshot.queryParamMap.get('returnUrl');
    return value && value.startsWith('/') && !value.startsWith('//') ? value : '/home';
  }
}
