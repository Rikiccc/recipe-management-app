import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MessageResponse, RegisterRequest } from '../models';

/** Talks to /api/auth: registration and account activation. */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  register(request: RegisterRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/register`, request);
  }

  activate(token: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/activate`, { token });
  }

  resendActivation(email: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/resend-activation`, { email });
  }
}