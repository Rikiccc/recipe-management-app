import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PantryRoutingModule } from './pantry-routing-module';
import { Pantry } from './pantry';

@NgModule({
  declarations: [Pantry],
  imports: [CommonModule, PantryRoutingModule],
})
export class PantryModule {}
