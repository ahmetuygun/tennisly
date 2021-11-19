import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICord } from '../cord.model';
import { CordService } from '../service/cord.service';

@Component({
  templateUrl: './cord-delete-dialog.component.html',
})
export class CordDeleteDialogComponent {
  cord?: ICord;

  constructor(protected cordService: CordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
