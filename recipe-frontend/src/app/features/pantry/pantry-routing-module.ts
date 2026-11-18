import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Pantry } from './pantry';

const routes: Routes = [{ path: '', component: Pantry }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PantryRoutingModule {}
