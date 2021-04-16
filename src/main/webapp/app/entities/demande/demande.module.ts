import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DemandeComponent } from './list/demande.component';
import { DemandeDetailComponent } from './detail/demande-detail.component';
import { DemandeUpdateComponent } from './update/demande-update.component';
import { DemandeDeleteDialogComponent } from './delete/demande-delete-dialog.component';
import { DemandeRoutingModule } from './route/demande-routing.module';

@NgModule({
  imports: [SharedModule, DemandeRoutingModule],
  declarations: [DemandeComponent, DemandeDetailComponent, DemandeUpdateComponent, DemandeDeleteDialogComponent],
  entryComponents: [DemandeDeleteDialogComponent],
})
export class DemandeModule {}
