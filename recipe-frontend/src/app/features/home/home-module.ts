import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTabsModule } from '@angular/material/tabs';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

import { HomeRoutingModule } from './home-routing-module';
import { Home } from './home';
import { SharedModule } from '../../shared/shared-module';

@NgModule({
  declarations: [Home],
  imports: [
    CommonModule,
    RouterModule,
    HomeRoutingModule,
    SharedModule,
    MatTabsModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
  ],
})
export class HomeModule {}
