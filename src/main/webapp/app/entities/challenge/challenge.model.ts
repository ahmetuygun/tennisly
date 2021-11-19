import * as dayjs from 'dayjs';
import { ICord } from 'app/entities/cord/cord.model';
import { IPlayer } from 'app/entities/player/player.model';
import { ChallengeStatus } from 'app/entities/enumerations/challenge-status.model';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';

export interface IChallenge {
  id?: number;
  matchTime?: dayjs.Dayjs | null;
  challengeStatus?: ChallengeStatus | null;
  status?: GeneralStatus | null;
  cord?: ICord | null;
  proposer?: IPlayer | null;
  acceptor?: IPlayer | null;
}

export class Challenge implements IChallenge {
  constructor(
    public id?: number,
    public matchTime?: dayjs.Dayjs | null,
    public challengeStatus?: ChallengeStatus | null,
    public status?: GeneralStatus | null,
    public cord?: ICord | null,
    public proposer?: IPlayer | null,
    public acceptor?: IPlayer | null
  ) {}
}

export function getChallengeIdentifier(challenge: IChallenge): number | undefined {
  return challenge.id;
}
