import { HttpErrorResponse } from '@angular/common/http';
import { FormGroup } from '@angular/forms';
import { ApiError } from '../models';

export function toErrorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    if (error.status === 0) {
      return 'Server nije dostupan. Proveri da li je backend pokrenut.';
    }
    const body = error.error as Partial<ApiError> | null;
    if (body?.message) {
      return body.message;
    }
    return `Greška ${error.status}: ${error.statusText}`;
  }
  return 'Došlo je do neočekivane greške.';
}

export function applyFieldErrors(form: FormGroup, error: unknown): boolean {
  if (!(error instanceof HttpErrorResponse)) {
    return false;
  }
  const fieldErrors = (error.error as Partial<ApiError> | null)?.fieldErrors;
  if (!fieldErrors) {
    return false;
  }
  let applied = false;
  for (const [field, message] of Object.entries(fieldErrors)) {
    const control = form.get(field);
    if (control) {
      control.setErrors({ server: message });
      control.markAsTouched();
      applied = true;
    }
  }
  return applied;
}