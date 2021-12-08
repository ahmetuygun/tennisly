import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IChallenge } from '../challenge.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';
import { ChallengeStatus } from '../../enumerations/challenge-status.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { ChallengeService } from '../service/challenge.service';

@Component({
  selector: 'jhi-challenge-detail',
  templateUrl: './challenge-detail.component.html',
})
export class ChallengeDetailComponent implements OnInit {
  challenge: IChallenge | null = null;
  currentAccount: Account | null = null;
  ChallengeStatus = ChallengeStatus;
  isSaving = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    protected challengeService: ChallengeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.activatedRoute.data.subscribe(({ challenge }) => {
      this.challenge = challenge;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const challenge = this.challenge;
    if (challenge?.id !== undefined) {
      this.subscribeToSaveResponse(this.challengeService.update(challenge));
    }
  }
  updateChallengeStatus(status: ChallengeStatus): void {
    this.isSaving = true;
    const challenge = this.challenge;
    if (challenge) {
      challenge.challengeStatus = status;
    }
    if (challenge?.id !== undefined) {
      this.subscribeToSaveResponse(this.challengeService.update(challenge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChallenge>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/challenge', this.challenge?.id, 'view']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
