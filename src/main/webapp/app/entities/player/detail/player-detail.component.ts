import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayer } from '../player.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { Level } from 'app/entities/enumerations/level.model';

@Component({
  selector: 'jhi-player-detail',
  templateUrl: './player-detail.component.html',
})
export class PlayerDetailComponent implements OnInit {
  player: IPlayer | null = null;
  Level = Level;
  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.player = player;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
