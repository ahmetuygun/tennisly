import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICord, Cord } from '../cord.model';
import { CordService } from '../service/cord.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';

@Component({
  selector: 'jhi-cord-update',
  templateUrl: './cord-update.component.html',
})
export class CordUpdateComponent implements OnInit {
  isSaving = false;
  generalStatusValues = Object.keys(GeneralStatus);

  editForm = this.fb.group({
    id: [],
    name: [],
    adress: [],
    image: [],
    imageContentType: [],
    status: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected cordService: CordService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cord }) => {
      this.updateForm(cord);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('tennislyApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cord = this.createFromForm();
    if (cord.id !== undefined) {
      this.subscribeToSaveResponse(this.cordService.update(cord));
    } else {
      this.subscribeToSaveResponse(this.cordService.create(cord));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICord>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cord: ICord): void {
    this.editForm.patchValue({
      id: cord.id,
      name: cord.name,
      adress: cord.adress,
      image: cord.image,
      imageContentType: cord.imageContentType,
      status: cord.status,
    });
  }

  protected createFromForm(): ICord {
    return {
      ...new Cord(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
