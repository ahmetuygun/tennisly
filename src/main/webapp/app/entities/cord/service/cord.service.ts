import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICord, getCordIdentifier } from '../cord.model';

export type EntityResponseType = HttpResponse<ICord>;
export type EntityArrayResponseType = HttpResponse<ICord[]>;

@Injectable({ providedIn: 'root' })
export class CordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cords');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cord: ICord): Observable<EntityResponseType> {
    return this.http.post<ICord>(this.resourceUrl, cord, { observe: 'response' });
  }

  update(cord: ICord): Observable<EntityResponseType> {
    return this.http.put<ICord>(`${this.resourceUrl}/${getCordIdentifier(cord) as number}`, cord, { observe: 'response' });
  }

  partialUpdate(cord: ICord): Observable<EntityResponseType> {
    return this.http.patch<ICord>(`${this.resourceUrl}/${getCordIdentifier(cord) as number}`, cord, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCordToCollectionIfMissing(cordCollection: ICord[], ...cordsToCheck: (ICord | null | undefined)[]): ICord[] {
    const cords: ICord[] = cordsToCheck.filter(isPresent);
    if (cords.length > 0) {
      const cordCollectionIdentifiers = cordCollection.map(cordItem => getCordIdentifier(cordItem)!);
      const cordsToAdd = cords.filter(cordItem => {
        const cordIdentifier = getCordIdentifier(cordItem);
        if (cordIdentifier == null || cordCollectionIdentifiers.includes(cordIdentifier)) {
          return false;
        }
        cordCollectionIdentifiers.push(cordIdentifier);
        return true;
      });
      return [...cordsToAdd, ...cordCollection];
    }
    return cordCollection;
  }
}
