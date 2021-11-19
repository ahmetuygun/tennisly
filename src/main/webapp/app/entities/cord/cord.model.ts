import { IChallenge } from 'app/entities/challenge/challenge.model';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';

export interface ICord {
  id?: number;
  name?: string | null;
  adress?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  status?: GeneralStatus | null;
  challenge?: IChallenge | null;
}

export class Cord implements ICord {
  constructor(
    public id?: number,
    public name?: string | null,
    public adress?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public status?: GeneralStatus | null,
    public challenge?: IChallenge | null
  ) {}
}

export function getCordIdentifier(cord: ICord): number | undefined {
  return cord.id;
}
