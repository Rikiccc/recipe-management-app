import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { LayoutModule } from './layout/layout-module';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { SerbianPaginatorIntl } from './core/i18n/paginator-intl';
import { authInterceptor } from './core/auth';

@NgModule({
  declarations: [
    App
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LayoutModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideClientHydration(),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
    { provide: MatPaginatorIntl, useClass: SerbianPaginatorIntl },
  ],
  bootstrap: [App]
})
export class AppModule { }