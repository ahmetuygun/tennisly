import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { Challenge, IChallenge } from '../challenge.model';
import { ChallengeService } from '../service/challenge.service';
import { ICord } from 'app/entities/cord/cord.model';
import { CordService } from 'app/entities/cord/service/cord.service';
import { IPlayer, Player } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ChallengeStatus } from 'app/entities/enumerations/challenge-status.model';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-challenge-update',
  templateUrl: './challenge-update.component.html',
})
export class ChallengeUpdateComponent implements OnInit {
  isSaving = false;
  challengeStatusValues = Object.keys(ChallengeStatus);
  generalStatusValues = Object.keys(GeneralStatus);

  cordsCollection: ICord[] = [];
  playersSharedCollection: IPlayer[] = [];
  account: Account | null = null;

  editForm = this.fb.group({
    id: [],
    matchTime: [],
    challengeStatus: [],
    status: [],
    cord: [],
    proposer: [],
    acceptor: [],
  });

  constructor(
    protected challengeService: ChallengeService,
    protected cordService: CordService,
    protected playerService: PlayerService,
    private accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ challenge }) => {
      if (challenge.id === undefined) {
        const today = dayjs().startOf('day');
        challenge.matchTime = today;
      }
      this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

      this.updateForm(challenge);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const challenge = this.createFromForm();
    challenge.challengeStatus = ChallengeStatus.REQUESTED;

    if (challenge.id !== undefined) {
      this.subscribeToSaveResponse(this.challengeService.update(challenge));
    } else {
      if (this.account?.playerId) {
        const proposer = new Player();
        proposer.id = this.account.playerId;
        challenge.proposer = proposer;
        challenge.challengeStatus = ChallengeStatus.REQUESTED;
        challenge.status = GeneralStatus.ACTIVE;
      }
      this.subscribeToSaveResponse(this.challengeService.create(challenge));
    }
  }

  trackCordById(index: number, item: ICord): number {
    return item.id!;
  }

  trackPlayerById(index: number, item: IPlayer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChallenge>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/challenge']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(challenge: IChallenge): void {
    this.editForm.patchValue({
      id: challenge.id,
      matchTime: challenge.matchTime ? challenge.matchTime.format(DATE_TIME_FORMAT) : null,
      challengeStatus: challenge.challengeStatus,
      status: challenge.status,
      cord: challenge.cord,
      proposer: challenge.proposer,
      acceptor: challenge.acceptor,
    });

    this.cordsCollection = this.cordService.addCordToCollectionIfMissing(this.cordsCollection, challenge.cord);
    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing(
      this.playersSharedCollection,
      challenge.proposer,
      challenge.acceptor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cordService
      .query({ 'challengeId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICord[]>) => res.body ?? []))
      .pipe(map((cords: ICord[]) => this.cordService.addCordToCollectionIfMissing(cords, this.editForm.get('cord')!.value)))
      .subscribe((cords: ICord[]) => (this.cordsCollection = cords));

    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(
        map((players: IPlayer[]) =>
          this.playerService.addPlayerToCollectionIfMissing(
            players,
            this.editForm.get('proposer')!.value,
            this.editForm.get('acceptor')!.value
          )
        )
      )
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));
  }

  protected createFromForm(): IChallenge {
    return {
      ...new Challenge(),
      id: this.editForm.get(['id'])!.value,
      matchTime: this.editForm.get(['matchTime'])!.value ? dayjs(this.editForm.get(['matchTime'])!.value, DATE_TIME_FORMAT) : undefined,
      challengeStatus: this.editForm.get(['challengeStatus'])!.value,
      status: this.editForm.get(['status'])!.value,
      cord: this.editForm.get(['cord'])!.value,
      proposer: this.editForm.get(['proposer'])!.value,
      acceptor: this.editForm.get(['acceptor'])!.value,
    };
  }
}
