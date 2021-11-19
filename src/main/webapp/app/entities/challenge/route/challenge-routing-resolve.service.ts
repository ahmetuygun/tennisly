import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChallenge, Challenge } from '../challenge.model';
import { ChallengeService } from '../service/challenge.service';
import { Player } from '../../player/player.model';

@Injectable({ providedIn: 'root' })
export class ChallengeRoutingResolveService implements Resolve<IChallenge> {
  constructor(protected service: ChallengeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChallenge> | Observable<never> {
    const id = route.params['id'];
    const playerId = route.params['playerId'];

    if (id) {
      return this.service.find(id).pipe(
        mergeMap((challenge: HttpResponse<Challenge>) => {
          if (challenge.body) {
            return of(challenge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    } else if (playerId) {
      const challenge = new Challenge();
      const accepter = new Player();
      accepter.id = playerId;
      challenge.acceptor = accepter;
      return of(challenge);
    }
    return of(new Challenge());
  }
}
