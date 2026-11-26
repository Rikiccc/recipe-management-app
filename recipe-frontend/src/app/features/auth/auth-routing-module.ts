import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { guestGuard } from '../../core/auth';
import { Activate } from './activate/activate';
import { Login } from './login/login';
import { Register } from './register/register';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login, canActivate: [guestGuard], title: 'Prijava' },
  { path: 'register', component: Register, canActivate: [guestGuard], title: 'Registracija' },
  // reachable while logged in too: the link in the e-mail must always work
  { path: 'activate', component: Activate, title: 'Aktivacija naloga' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {}