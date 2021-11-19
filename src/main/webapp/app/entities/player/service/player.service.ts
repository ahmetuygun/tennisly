import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayer, getPlayerIdentifier } from '../player.model';

export type EntityResponseType = HttpResponse<IPlayer>;
export type EntityArrayResponseType = HttpResponse<IPlayer[]>;

@Injectable({ providedIn: 'root' })
export class PlayerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/players');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(player: IPlayer): Observable<EntityResponseType> {
    return this.http.post<IPlayer>(this.resourceUrl, player, { observe: 'response' });
  }

  update(player: IPlayer): Observable<EntityResponseType> {
    return this.http.put<IPlayer>(`${this.resourceUrl}/${getPlayerIdentifier(player) as number}`, player, { observe: 'response' });
  }

  partialUpdate(player: IPlayer): Observable<EntityResponseType> {
    return this.http.patch<IPlayer>(`${this.resourceUrl}/${getPlayerIdentifier(player) as number}`, player, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayerToCollectionIfMissing(playerCollection: IPlayer[], ...playersToCheck: (IPlayer | null | undefined)[]): IPlayer[] {
    const players: IPlayer[] = playersToCheck.filter(isPresent);
    if (players.length > 0) {
      const playerCollectionIdentifiers = playerCollection.map(playerItem => getPlayerIdentifier(playerItem)!);
      const playersToAdd = players.filter(playerItem => {
        const playerIdentifier = getPlayerIdentifier(playerItem);
        if (playerIdentifier == null || playerCollectionIdentifiers.includes(playerIdentifier)) {
          return false;
        }
        playerCollectionIdentifiers.push(playerIdentifier);
        return true;
      });
      return [...playersToAdd, ...playerCollection];
    }
    return playerCollection;
  }
}
