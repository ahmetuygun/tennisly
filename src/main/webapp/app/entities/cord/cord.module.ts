import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CordComponent } from './list/cord.component';
import { CordDetailComponent } from './detail/cord-detail.component';
import { CordUpdateComponent } from './update/cord-update.component';
import { CordDeleteDialogComponent } from './delete/cord-delete-dialog.component';
import { CordRoutingModule } from './route/cord-routing.module';

@NgModule({
  imports: [SharedModule, CordRoutingModule],
  declarations: [CordComponent, CordDetailComponent, CordUpdateComponent, CordDeleteDialogComponent],
  entryComponents: [CordDeleteDialogComponent],
})
export class CordModule {}
