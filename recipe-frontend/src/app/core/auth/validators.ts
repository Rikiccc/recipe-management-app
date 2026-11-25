import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/** Same rule as backend (PasswordRules): 8-72 characters, at least one letter and one digit. */
const PASSWORD_REGEX = /^(?=.*\p{L})(?=.*\d).{8,72}$/u;
const USERNAME_REGEX = /^[A-Za-z0-9_.-]+$/;

export const passwordRules: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const value = control.value as string;
  return !value || PASSWORD_REGEX.test(value) ? null : { passwordRules: true };
};

export const usernameRules: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const value = control.value as string;
  return !value || USERNAME_REGEX.test(value) ? null : { usernameRules: true };
};

/** Group validator: marks `confirm` as wrong when it differs from `password`. */
export function fieldsMatch(passwordKey: string, confirmKey: string): ValidatorFn {
  return (group: AbstractControl): ValidationErrors | null => {
    const password = group.get(passwordKey);
    const confirm = group.get(confirmKey);
    if (!password || !confirm) {
      return null;
    }
    const mismatch = !!confirm.value && password.value !== confirm.value;
    const errors = { ...(confirm.errors ?? {}) };
    if (mismatch) {
      errors['mismatch'] = true;
    } else {
      delete errors['mismatch'];
    }
    confirm.setErrors(Object.keys(errors).length ? errors : null);
    return null;
  };
}

/** Readable message (Serbian) for the first error of a control, or null. */
export function controlError(control: AbstractControl | null): string | null {
  if (!control?.errors) {
    return null;
  }
  const errors = control.errors;
  if (errors['server']) {
    return String(errors['server']);
  }
  if (errors['required']) {
    return 'Polje je obavezno.';
  }
  if (errors['email']) {
    return 'E-pošta nije ispravna.';
  }
  if (errors['usernameRules']) {
    return 'Dozvoljena su slova, brojevi, tačka, crta i donja crta.';
  }
  if (errors['passwordRules']) {
    return 'Lozinka mora imati 8-72 karaktera, bar jedno slovo i jednu cifru.';
  }
  if (errors['mismatch']) {
    return 'Lozinke se ne poklapaju.';
  }
  if (errors['minlength']) {
    return `Najmanje ${(errors['minlength'] as { requiredLength: number }).requiredLength} karaktera.`;
  }
  if (errors['maxlength']) {
    return `Najviše ${(errors['maxlength'] as { requiredLength: number }).requiredLength} karaktera.`;
  }
  return 'Vrednost nije ispravna.';
}
