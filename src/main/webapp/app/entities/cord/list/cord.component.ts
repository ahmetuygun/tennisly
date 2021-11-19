import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICord } from '../cord.model';
import { CordService } from '../service/cord.service';
import { CordDeleteDialogComponent } from '../delete/cord-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cord',
  templateUrl: './cord.component.html',
})
export class CordComponent implements OnInit {
  cords?: ICord[];
  isLoading = false;

  constructor(protected cordService: CordService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cordService.query().subscribe(
      (res: HttpResponse<ICord[]>) => {
        this.isLoading = false;
        this.cords = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICord): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(cord: ICord): void {
    const modalRef = this.modalService.open(CordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cord = cord;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
