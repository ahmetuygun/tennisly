import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { Level } from 'app/entities/enumerations/level.model';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';

export interface IPlayer {
  id?: number;
  fullName?: string | null;
  gender?: Gender | null;
  level?: Level | null;
  phone?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  status?: GeneralStatus | null;
  internalUser?: IUser | null;
}

export class Player implements IPlayer {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public gender?: Gender | null,
    public level?: Level | null,
    public phone?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public status?: GeneralStatus | null,
    public internalUser?: IUser | null
  ) {}
}

export function getPlayerIdentifier(player: IPlayer): number | undefined {
  return player.id;
}
