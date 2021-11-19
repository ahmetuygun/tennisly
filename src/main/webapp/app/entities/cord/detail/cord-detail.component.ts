import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICord } from '../cord.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cord-detail',
  templateUrl: './cord-detail.component.html',
})
export class CordDetailComponent implements OnInit {
  cord: ICord | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cord }) => {
      this.cord = cord;
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
