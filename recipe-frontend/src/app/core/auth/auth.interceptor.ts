import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

const CREDENTIAL_ENDPOINTS = ['/auth/login', '/auth/register', '/auth/activate', '/auth/reset-password'];

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const isApiCall = request.url.startsWith(environment.apiUrl);
  const token = auth.token();

  const authorized =
    isApiCall && token && !request.headers.has('Authorization')
      ? request.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : request;

  return next(authorized).pipe(
    catchError((error: unknown) => {
      if (
        error instanceof HttpErrorResponse &&
        error.status === 401 &&
        isApiCall &&
        auth.hasSession() &&
        !CREDENTIAL_ENDPOINTS.some((path) => request.url.includes(path))
      ) {
        const returnUrl = router.url;
        auth.clearSession();
        void router.navigate(['/auth/login'], {
          queryParams: { reason: 'expired', ...(returnUrl.startsWith('/auth') ? {} : { returnUrl }) },
        });
      }
      return throwError(() => error);
    }),
  );
};
