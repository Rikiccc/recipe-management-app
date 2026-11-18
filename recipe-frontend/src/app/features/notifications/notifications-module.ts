import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotificationsRoutingModule } from './notifications-routing-module';
import { Notifications } from './notifications';

@NgModule({
  declarations: [Notifications],
  imports: [CommonModule, NotificationsRoutingModule],
})
export class NotificationsModule {}
