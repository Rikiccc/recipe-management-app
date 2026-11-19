import { Role } from './role';

/**
 * User as seen by frontend (no password)
 */
export interface User {
  id: number;
  username: string;
  email: string;
  role: Role;
  enabled: boolean;
  profileImage?: string;
  region?: string;
  biography?: string;
}
