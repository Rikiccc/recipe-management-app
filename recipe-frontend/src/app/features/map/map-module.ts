import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing-module';
import { Map } from './map';

@NgModule({
  declarations: [Map],
  imports: [CommonModule, MapRoutingModule],
})
export class MapModule {}
