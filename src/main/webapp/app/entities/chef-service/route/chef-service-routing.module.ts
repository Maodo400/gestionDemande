import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChefServiceComponent } from '../list/chef-service.component';
import { ChefServiceDetailComponent } from '../detail/chef-service-detail.component';
import { ChefServiceUpdateComponent } from '../update/chef-service-update.component';
import { ChefServiceRoutingResolveService } from './chef-service-routing-resolve.service';

const chefServiceRoute: Routes = [
  {
    path: '',
    component: ChefServiceComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChefServiceDetailComponent,
    resolve: {
      chefService: ChefServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChefServiceUpdateComponent,
    resolve: {
      chefService: ChefServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChefServiceUpdateComponent,
    resolve: {
      chefService: ChefServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chefServiceRoute)],
  exports: [RouterModule],
})
export class ChefServiceRoutingModule {}
