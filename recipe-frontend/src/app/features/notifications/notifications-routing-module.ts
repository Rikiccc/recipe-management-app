import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Notifications } from './notifications';

const routes: Routes = [{ path: '', component: Notifications }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NotificationsRoutingModule {}
