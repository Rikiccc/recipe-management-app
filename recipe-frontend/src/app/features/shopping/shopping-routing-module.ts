import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Shopping } from './shopping';

const routes: Routes = [{ path: '', component: Shopping }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ShoppingRoutingModule {}
