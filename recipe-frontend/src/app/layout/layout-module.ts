import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';

import { MainLayout } from './main-layout/main-layout';
import { Header } from './header/header';
import { Footer } from './footer/footer';
import { NotFound } from './not-found/not-found';

@NgModule({
  declarations: [MainLayout, Header, Footer, NotFound],
  imports: [CommonModule, RouterModule, MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule],
  exports: [MainLayout, NotFound],
})
export class LayoutModule {}
