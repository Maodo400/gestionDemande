import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeComponent } from '../list/demande.component';
import { DemandeDetailComponent } from '../detail/demande-detail.component';
import { DemandeUpdateComponent } from '../update/demande-update.component';
import { DemandeRoutingResolveService } from './demande-routing-resolve.service';

const demandeRoute: Routes = [
  {
    path: '',
    component: DemandeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeDetailComponent,
    resolve: {
      demande: DemandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeUpdateComponent,
    resolve: {
      demande: DemandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeUpdateComponent,
    resolve: {
      demande: DemandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeRoute)],
  exports: [RouterModule],
})
export class DemandeRoutingModule {}
