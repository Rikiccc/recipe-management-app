import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    const role = localStorage.getItem('userRole');
    
    if (role && role !== 'ADMIN') {
      return this.router.createUrlTree(['/home']);
    }
    return true;
  }
}