import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CordComponent } from '../list/cord.component';
import { CordDetailComponent } from '../detail/cord-detail.component';
import { CordUpdateComponent } from '../update/cord-update.component';
import { CordRoutingResolveService } from './cord-routing-resolve.service';

const cordRoute: Routes = [
  {
    path: '',
    component: CordComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CordDetailComponent,
    resolve: {
      cord: CordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CordUpdateComponent,
    resolve: {
      cord: CordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CordUpdateComponent,
    resolve: {
      cord: CordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cordRoute)],
  exports: [RouterModule],
})
export class CordRoutingModule {}
