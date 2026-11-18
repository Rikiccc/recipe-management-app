import { HttpErrorResponse } from '@angular/common/http';
import { ApiError } from '../models';


/** Converts error from HttpClient to a readable message */
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
