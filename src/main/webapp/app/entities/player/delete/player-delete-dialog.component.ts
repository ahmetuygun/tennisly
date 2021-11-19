import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayer } from '../player.model';
import { PlayerService } from '../service/player.service';

@Component({
  templateUrl: './player-delete-dialog.component.html',
})
export class PlayerDeleteDialogComponent {
  player?: IPlayer;

  constructor(protected playerService: PlayerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
