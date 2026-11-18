import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RecipesRoutingModule } from './recipes-routing-module';
import { Recipes } from './recipes';

@NgModule({
  declarations: [Recipes],
  imports: [CommonModule, RecipesRoutingModule],
})
export class RecipesModule {}
