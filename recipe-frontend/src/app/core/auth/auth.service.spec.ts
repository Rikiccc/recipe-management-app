import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';
import { fakeAuthResponse } from './auth.testing';

const STORAGE_KEY = 'recipes.session';

describe('AuthService', () => {
  let http: HttpTestingController;

  function setup(): AuthService {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([{ path: 'home', children: [] }])],
    });
    http = TestBed.inject(HttpTestingController);
    return TestBed.inject(AuthService);
  }

  beforeEach(() => localStorage.clear());

  it('stores the session after login', () => {
    const auth = setup();
    expect(auth.isLoggedIn()).toBe(false);

    auth.login({ usernameOrEmail: 'pera', password: 'x' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/login`).flush(fakeAuthResponse());

    expect(auth.isLoggedIn()).toBe(true);
    expect(auth.token()).toBe('jwt-token');
    expect(auth.user()?.username).toBe('pera');
    expect(JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '{}').token).toBe('jwt-token');
  });

  it('restores a valid session from storage', () => {
    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({ token: 't', expiresAt: Date.now() + 60_000, user: fakeAuthResponse().user }),
    );
    const auth = setup();
    expect(auth.isLoggedIn()).toBe(true);
    expect(auth.token()).toBe('t');
  });

  it('ignores an expired or broken session in storage', () => {
    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({ token: 't', expiresAt: Date.now() - 1000, user: fakeAuthResponse().user }),
    );
    expect(setup().isLoggedIn()).toBe(false);

    TestBed.resetTestingModule();
    localStorage.setItem(STORAGE_KEY, '{not json');
    expect(setup().isLoggedIn()).toBe(false);
  });

  it('knows administrators', () => {
    const auth = setup();
    auth.login({ usernameOrEmail: 'admin', password: 'x' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/login`).flush(fakeAuthResponse({}, { role: 'ADMIN' as never }));
    expect(auth.isAdmin()).toBe(true);
  });

  it('logout forgets the session at once and revokes the token on the server', () => {
    const auth = setup();
    auth.login({ usernameOrEmail: 'pera', password: 'x' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/login`).flush(fakeAuthResponse());

    auth.logout();

    expect(auth.isLoggedIn()).toBe(false);
    expect(localStorage.getItem(STORAGE_KEY)).toBeNull();
    const request = http.expectOne(`${environment.apiUrl}/auth/logout`);
    expect(request.request.headers.get('Authorization')).toBe('Bearer jwt-token');
    request.flush(null);
  });

  it('keeps working with the new token after the password change', () => {
    const auth = setup();
    auth.login({ usernameOrEmail: 'pera', password: 'x' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/login`).flush(fakeAuthResponse());

    auth.changePassword({ currentPassword: 'a', newPassword: 'b' }).subscribe();
    http.expectOne(`${environment.apiUrl}/auth/change-password`).flush(fakeAuthResponse({ token: 'new-token' }));

    expect(auth.token()).toBe('new-token');
  });
});
