import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, PLATFORM_ID, computed, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { Observable, catchError, of, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AuthResponse,
  LoginRequest,
  MessageResponse,
  RegisterRequest,
  Role,
  User,
} from '../models';

const STORAGE_KEY = 'recipes.session';

interface Session {
  token: string;
  /** Expiry as epoch milliseconds. */
  expiresAt: number;
  user: User;
}

/**
 * Keeps the logged in user and JWT, and talks to /api/auth.
 *
 * The session is kept in localStorage so it survives a page reload. Everything stored there can be
 * edited by the user, so it is only used to decide what the UI shows. The backend checks the token
 * on every request and is the one that really enforces access.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  private readonly session = signal<Session | null>(this.restore());

  readonly user = computed(() => this.session()?.user ?? null);
  readonly token = computed(() => this.session()?.token ?? null);
  readonly isAdmin = computed(() => this.session()?.user.role === Role.Admin);

  constructor() {
    if (this.isBrowser) {
      // keep tabs in sync: login/logout in one tab is visible in the others
      window.addEventListener('storage', (event) => {
        if (event.key === STORAGE_KEY) {
          this.session.set(this.restore());
        }
      });
    }
  }

  /** True while a token exists and has not expired. Safe to call from guards (synchronous). */
  isLoggedIn(): boolean {
    const session = this.session();
    if (!session) {
      return false;
    }
    if (session.expiresAt <= Date.now()) {
      this.clearSession();
      return false;
    }
    return true;
  }

  /** Whether the app currently holds any token, expired or not. Used by the 401 handler. */
  hasSession(): boolean {
    return this.session() !== null;
  }

  // ------------------------------------------------------------------ API calls

  register(request: RegisterRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/register`, request);
  }

  activate(token: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/activate`, { token });
  }

  resendActivation(email: string): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/resend-activation`, { email });
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/login`, request)
      .pipe(tap((response) => this.startSession(response)));
  }

  /**
   * Logs out: tells the backend to revoke the token, and forgets it locally right away
   * (even if the request fails, the user must end up logged out).
   */
  logout(): void {
    const token = this.token();
    this.clearSession();
    if (token) {
      this.http
        .post<void>(`${this.baseUrl}/logout`, null, {
          headers: new HttpHeaders({ Authorization: `Bearer ${token}` }),
        })
        .pipe(catchError(() => of(null)))
        .subscribe();
    }
    void this.router.navigate(['/home']);
  }

  /** Replaces the stored user, e.g. after the profile was edited. */
  updateUser(user: User): void {
    const current = this.session();
    if (current) {
      this.save({ ...current, user });
    }
  }

  /** Forgets the session without calling the backend (token already rejected or expired). */
  clearSession(): void {
    this.session.set(null);
    if (this.isBrowser) {
      try {
        localStorage.removeItem(STORAGE_KEY);
      } catch {
        // storage blocked, nothing to clean
      }
    }
  }

  // ------------------------------------------------------------------ storage

  private startSession(response: AuthResponse): void {
    this.save({
      token: response.token,
      expiresAt: Date.parse(response.expiresAt),
      user: response.user,
    });
  }

  private save(session: Session): void {
    this.session.set(session);
    if (this.isBrowser) {
      try {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
      } catch {
        // storage blocked: user stays logged in until reload
      }
    }
  }

  private restore(): Session | null {
    if (!this.isBrowser) {
      return null;
    }
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) {
        return null;
      }
      const parsed = JSON.parse(raw) as Partial<Session>;
      if (
        typeof parsed.token !== 'string' ||
        typeof parsed.expiresAt !== 'number' ||
        !parsed.user ||
        parsed.expiresAt <= Date.now()
      ) {
        localStorage.removeItem(STORAGE_KEY);
        return null;
      }
      return parsed as Session;
    } catch {
      return null;
    }
  }
}