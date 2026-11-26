import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { environment } from '../../../environments/environment';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from './auth.service';
import { fakeAuthResponse } from './auth.testing';

describe('authInterceptor', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
  let auth: AuthService;
  let router: Router;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    });
    http = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
    auth = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  function logIn(): void {
    auth.login({ usernameOrEmail: 'pera', password: 'x' }).subscribe();
    controller.expectOne(`${environment.apiUrl}/auth/login`).flush(fakeAuthResponse());
  }

  it('sends no Authorization header when logged out', () => {
    http.get(`${environment.apiUrl}/recipes/newest`).subscribe();
    expect(controller.expectOne(`${environment.apiUrl}/recipes/newest`).request.headers.has('Authorization')).toBe(
      false,
    );
  });

  it('adds the bearer token to calls to our backend', () => {
    logIn();
    http.get(`${environment.apiUrl}/users/me`).subscribe();
    expect(controller.expectOne(`${environment.apiUrl}/users/me`).request.headers.get('Authorization')).toBe(
      'Bearer jwt-token',
    );
  });

  it('never sends the token to another site', () => {
    logIn();
    http.get('https://example.com/data').subscribe();
    expect(controller.expectOne('https://example.com/data').request.headers.has('Authorization')).toBe(false);
  });

  it('drops the session and goes to login when backend answers 401', () => {
    const navigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);
    logIn();

    http.get(`${environment.apiUrl}/users/me`).subscribe({ error: () => undefined });
    controller.expectOne(`${environment.apiUrl}/users/me`).flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(auth.isLoggedIn()).toBe(false);
    expect(navigate).toHaveBeenCalledWith(['/auth/login'], expect.objectContaining({ queryParams: expect.anything() }));
  });

  it('does not treat a failed login as an expired session', () => {
    const navigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);
    logIn();

    http.post(`${environment.apiUrl}/auth/login`, {}).subscribe({ error: () => undefined });
    controller.expectOne(`${environment.apiUrl}/auth/login`).flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(navigate).not.toHaveBeenCalled();
    expect(auth.isLoggedIn()).toBe(true);
  });

  it('leaves 403 to the caller', () => {
    const navigate = vi.spyOn(router, 'navigate').mockResolvedValue(true);
    logIn();

    http.get(`${environment.apiUrl}/admin/users`).subscribe({ error: () => undefined });
    controller.expectOne(`${environment.apiUrl}/admin/users`).flush({}, { status: 403, statusText: 'Forbidden' });

    expect(navigate).not.toHaveBeenCalled();
    expect(auth.isLoggedIn()).toBe(true);
  });
});
