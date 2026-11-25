import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFound } from './layout/not-found/not-found';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth-module').then((m) => m.AuthModule),
  },
  {
    path: 'profile',
    loadChildren: () => import('./features/profile/profile-module').then((m) => m.ProfileModule),
  },
  {
    path: 'recipes',
    loadChildren: () => import('./features/recipes/recipes-module').then((m) => m.RecipesModule),
  },
  {
    path: 'home',
    loadChildren: () => import('./features/home/home-module').then((m) => m.HomeModule),
  },
  {
    path: 'search',
    loadChildren: () => import('./features/search/search-module').then((m) => m.SearchModule),
  },
  {
    path: 'pantry',
    loadChildren: () => import('./features/pantry/pantry-module').then((m) => m.PantryModule),
  },
  {
    path: 'shopping',
    loadChildren: () => import('./features/shopping/shopping-module').then((m) => m.ShoppingModule),
  },
  {
    path: 'reports',
    loadChildren: () => import('./features/reports/reports-module').then((m) => m.ReportsModule),
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin-module').then((m) => m.AdminModule),
  },
  { path: 'map', loadChildren: () => import('./features/map/map-module').then((m) => m.MapModule) },
  {
    path: 'notifications',
    loadChildren: () =>
      import('./features/notifications/notifications-module').then((m) => m.NotificationsModule),
  },
  { path: '**', component: NotFound },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
