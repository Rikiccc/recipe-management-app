import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, provideRouter } from '@angular/router';
import { environment } from '../../../environments/environment';
import { adminGuard, authGuard, guestGuard } from './auth.guards';
import { AuthService } from './auth.service';
import { fakeAuthResponse } from './auth.testing';

describe('route guards', () => {
  let auth: AuthService;
  let controller: HttpTestingController;

  const route = {} as ActivatedRouteSnapshot;
  const state = { url: '/profile' } as RouterStateSnapshot;

  const run = (guard: typeof authGuard) => TestBed.runInInjectionContext(() => guard(route, state));
  const asPath = (result: unknown) => (result as UrlTree).toString();

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    auth = TestBed.inject(AuthService);
    controller = TestBed.inject(HttpTestingController);
  });

  function logIn(role: 'USER' | 'ADMIN'): void {
    auth.login({ usernameOrEmail: 'x', password: 'y' }).subscribe();
    controller
      .expectOne(`${environment.apiUrl}/auth/login`)
      .flush(fakeAuthResponse({}, { role: role as never }));
  }

  describe('authGuard', () => {
    it('sends a guest to login and remembers where he wanted to go', () => {
      expect(asPath(run(authGuard))).toBe('/auth/login?returnUrl=%2Fprofile');
    });

    it('lets a logged in user in', () => {
      logIn('USER');
      expect(run(authGuard)).toBe(true);
    });
  });

  describe('adminGuard', () => {
    it('sends a guest to login', () => {
      expect(asPath(run(adminGuard))).toContain('/auth/login');
    });

    it('sends a regular user home', () => {
      logIn('USER');
      expect(asPath(run(adminGuard))).toBe('/home');
    });

    it('lets an administrator in', () => {
      logIn('ADMIN');
      expect(run(adminGuard)).toBe(true);
    });
  });

  describe('guestGuard', () => {
    it('lets a guest see the login page', () => {
      expect(run(guestGuard)).toBe(true);
    });

    it('sends a logged in user home', () => {
      logIn('USER');
      expect(asPath(run(guestGuard))).toBe('/home');
    });
  });
});
