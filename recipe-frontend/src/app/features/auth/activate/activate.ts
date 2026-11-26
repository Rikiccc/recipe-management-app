import { Component, OnInit, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService, controlError } from '../../../core/auth';
import { applyFieldErrors, toErrorMessage } from '../../../core/services/api-error.util';

type ActivateState = 'activating' | 'success' | 'failed' | 'resend';

/**
 * Target of the link in the activation e-mail (/auth/activate?token=...).
 * Without a token it shows a form to request a new link.
 */
@Component({
  selector: 'app-activate',
  standalone: false,
  styleUrl: '../auth-forms.scss',
  templateUrl: './activate.html',
})
export class Activate implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(NonNullableFormBuilder);

  readonly errorOf = controlError;

  readonly state = signal<ActivateState>('resend');
  readonly message = signal<string | null>(null);
  readonly sending = signal(false);
  readonly resendMessage = signal<string | null>(null);

  readonly form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
  });

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      return;
    }
    this.state.set('activating');
    this.auth.activate(token).subscribe({
      next: (response) => {
        this.message.set(response.message);
        this.state.set('success');
      },
      error: (err: unknown) => {
        this.message.set(toErrorMessage(err));
        this.state.set('failed');
      },
    });
  }

  showResend(): void {
    this.message.set(null);
    this.state.set('resend');
  }

  resend(): void {
    if (this.form.invalid || this.sending()) {
      this.form.markAllAsTouched();
      return;
    }
    this.sending.set(true);
    this.resendMessage.set(null);
    this.auth.resendActivation(this.form.getRawValue().email).subscribe({
      next: (response) => {
        this.resendMessage.set(response.message);
        this.sending.set(false);
      },
      error: (err: unknown) => {
        this.sending.set(false);
        if (!applyFieldErrors(this.form, err)) {
          this.resendMessage.set(toErrorMessage(err));
        }
      },
    });
  }
}
