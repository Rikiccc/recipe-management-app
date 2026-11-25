import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Activate } from './activate/activate';
import { Register } from './register/register';

const routes: Routes = [
  { path: '', redirectTo: 'register', pathMatch: 'full' },
  { path: 'register', component: Register, title: 'Registracija' },
  { path: 'activate', component: Activate, title: 'Aktivacija naloga' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {}