import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ChefServiceComponent } from './list/chef-service.component';
import { ChefServiceDetailComponent } from './detail/chef-service-detail.component';
import { ChefServiceUpdateComponent } from './update/chef-service-update.component';
import { ChefServiceDeleteDialogComponent } from './delete/chef-service-delete-dialog.component';
import { ChefServiceRoutingModule } from './route/chef-service-routing.module';

@NgModule({
  imports: [SharedModule, ChefServiceRoutingModule],
  declarations: [ChefServiceComponent, ChefServiceDetailComponent, ChefServiceUpdateComponent, ChefServiceDeleteDialogComponent],
  entryComponents: [ChefServiceDeleteDialogComponent],
})
export class ChefServiceModule {}
