import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICord, Cord } from '../cord.model';
import { CordService } from '../service/cord.service';

@Injectable({ providedIn: 'root' })
export class CordRoutingResolveService implements Resolve<ICord> {
  constructor(protected service: CordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cord: HttpResponse<Cord>) => {
          if (cord.body) {
            return of(cord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cord());
  }
}
