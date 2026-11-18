import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ShoppingRoutingModule } from './shopping-routing-module';
import { Shopping } from './shopping';

@NgModule({
  declarations: [Shopping],
  imports: [CommonModule, ShoppingRoutingModule],
})
export class ShoppingModule {}
