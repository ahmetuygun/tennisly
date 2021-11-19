import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChallenge } from '../challenge.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-challenge-detail',
  templateUrl: './challenge-detail.component.html',
})
export class ChallengeDetailComponent implements OnInit {
  challenge: IChallenge | null = null;
  currentAccount: Account | null = null;

  constructor(protected activatedRoute: ActivatedRoute, private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.activatedRoute.data.subscribe(({ challenge }) => {
      this.challenge = challenge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
