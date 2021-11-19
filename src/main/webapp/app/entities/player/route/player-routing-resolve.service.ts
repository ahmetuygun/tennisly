import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayer, Player } from '../player.model';
import { PlayerService } from '../service/player.service';

@Injectable({ providedIn: 'root' })
export class PlayerRoutingResolveService implements Resolve<IPlayer> {
  constructor(protected service: PlayerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((player: HttpResponse<Player>) => {
          if (player.body) {
            return of(player.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Player());
  }
}
