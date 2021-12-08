import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayer, Player } from '../player.model';
import { PlayerService } from '../service/player.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Gender, GenderLabelMapping } from 'app/entities/enumerations/gender.model';
import { Level, LevelLabelMapping } from 'app/entities/enumerations/level.model';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';
import { User } from '../../../admin/user-management/user-management.model';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html',
})
export class PlayerUpdateComponent implements OnInit {
  account: Account | null = null;

  isSaving = false;

  public GenderLabelMapping = GenderLabelMapping;
  genderValues = Object.values(Gender);

  public LevelLabelMapping = LevelLabelMapping;
  levelValues = Object.values(Level);
  generalStatusValues = Object.keys(GeneralStatus);

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [],
    gender: [],
    level: [],
    phone: [],
    photo: [],
    photoContentType: [],
    status: [],
    internalUser: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected playerService: PlayerService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);
      this.loadRelationshipsOptions();
      this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
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
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      received => {
        this.onSaveSuccess(received.body);
      },
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(player: IPlayer | null): void {
    if (player?.id && this.account) {
      this.account.playerId = player.id;
      this.accountService.save(this.account).subscribe(() => {
        this.accountService.authenticate(this.account);
      });
    }

    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(player: IPlayer): void {
    this.editForm.patchValue({
      id: player.id,
      fullName: player.fullName,
      gender: player.gender,
      level: player.level,
      phone: player.phone,
      photo: player.photo,
      photoContentType: player.photoContentType,
      status: player.status,
      internalUser: player.internalUser,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, player.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPlayer {
    const user = new User();
    if (this.account) {
      if (this.account.id) {
        user.id = this.account.id;
      }
    }
    return {
      ...new Player(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      level: this.editForm.get(['level'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      status: this.editForm.get(['status'])!.value,
      internalUser: user,
    };
  }
}
