import { Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { AuthService, controlError, fieldsMatch, passwordRules, usernameRules } from '../../../core/auth';
import { applyFieldErrors, toErrorMessage } from '../../../core/services/api-error.util';

@Component({
  selector: 'app-register',
  standalone: false,
  styleUrl: '../auth-forms.scss',
  templateUrl: './register.html',
})
export class Register {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly auth = inject(AuthService);

  readonly errorOf = controlError;

  readonly form = this.fb.group(
    {
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30), usernameRules]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]],
      password: ['', [Validators.required, passwordRules]],
      confirmPassword: ['', [Validators.required]],
    },
    { validators: fieldsMatch('password', 'confirmPassword') },
  );

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly done = signal<string | null>(null);
  readonly hidePassword = signal(true);

  /** E-mail the activation link was sent to (shown after success). */
  sentTo = '';

  submit(): void {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }
    const { username, email, password } = this.form.getRawValue();
    this.loading.set(true);
    this.error.set(null);

    this.auth.register({ username, email, password }).subscribe({
      next: (response) => {
        this.sentTo = email;
        this.done.set(response.message);
        this.loading.set(false);
      },
      error: (err: unknown) => {
        this.loading.set(false);
        if (!applyFieldErrors(this.form, err)) {
          this.error.set(toErrorMessage(err));
        }
      },
    });
  }
}
