import { AuthResponse, Role, User } from '../models';

export function fakeUser(overrides: Partial<User> = {}): User {
  return { id: 1, username: 'pera', email: 'pera@example.com', role: Role.User, enabled: true, ...overrides };
}

export function fakeAuthResponse(overrides: Partial<AuthResponse> = {}, user: Partial<User> = {}): AuthResponse {
  return {
    token: 'jwt-token',
    tokenType: 'Bearer',
    expiresAt: new Date(Date.now() + 60 * 60 * 1000).toISOString(),
    user: fakeUser(user),
    ...overrides,
  };
}
