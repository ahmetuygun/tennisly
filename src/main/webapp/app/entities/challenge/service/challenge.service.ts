import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChallenge, getChallengeIdentifier } from '../challenge.model';

export type EntityResponseType = HttpResponse<IChallenge>;
export type EntityArrayResponseType = HttpResponse<IChallenge[]>;

@Injectable({ providedIn: 'root' })
export class ChallengeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/challenges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(challenge: IChallenge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(challenge);
    return this.http
      .post<IChallenge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(challenge: IChallenge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(challenge);
    return this.http
      .put<IChallenge>(`${this.resourceUrl}/${getChallengeIdentifier(challenge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(challenge: IChallenge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(challenge);
    return this.http
      .patch<IChallenge>(`${this.resourceUrl}/${getChallengeIdentifier(challenge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChallenge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChallenge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChallengeToCollectionIfMissing(
    challengeCollection: IChallenge[],
    ...challengesToCheck: (IChallenge | null | undefined)[]
  ): IChallenge[] {
    const challenges: IChallenge[] = challengesToCheck.filter(isPresent);
    if (challenges.length > 0) {
      const challengeCollectionIdentifiers = challengeCollection.map(challengeItem => getChallengeIdentifier(challengeItem)!);
      const challengesToAdd = challenges.filter(challengeItem => {
        const challengeIdentifier = getChallengeIdentifier(challengeItem);
        if (challengeIdentifier == null || challengeCollectionIdentifiers.includes(challengeIdentifier)) {
          return false;
        }
        challengeCollectionIdentifiers.push(challengeIdentifier);
        return true;
      });
      return [...challengesToAdd, ...challengeCollection];
    }
    return challengeCollection;
  }

  protected convertDateFromClient(challenge: IChallenge): IChallenge {
    return Object.assign({}, challenge, {
      matchTime: challenge.matchTime?.isValid() ? challenge.matchTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.matchTime = res.body.matchTime ? dayjs(res.body.matchTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((challenge: IChallenge) => {
        challenge.matchTime = challenge.matchTime ? dayjs(challenge.matchTime) : undefined;
      });
    }
    return res;
  }
}
