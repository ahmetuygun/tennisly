import { Gender } from './gender.model';

export enum ChallengeStatus {
  REQUESTED = 'REQUESTED',

  ACCEPTED = 'ACCEPTED',

  REJECTED = 'REJECTED',

  CANCELLED = 'CANCELLED',
}

export const ChallengeStatusLabelMapping: Record<ChallengeStatus, string> = {
  [ChallengeStatus.REQUESTED]: 'Teklif',
  [ChallengeStatus.ACCEPTED]: 'Kabul',
  [ChallengeStatus.REJECTED]: 'Red',
  [ChallengeStatus.CANCELLED]: 'İptal',
};
