import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';

import { RecipeCard } from './components/recipe-card/recipe-card';


@NgModule({
  declarations: [RecipeCard],
  imports: [CommonModule, RouterModule, MatCardModule, MatChipsModule, MatIconModule],
  exports: [RecipeCard],
})
export class SharedModule {}
