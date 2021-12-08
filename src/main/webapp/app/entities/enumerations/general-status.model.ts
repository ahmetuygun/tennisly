import { Gender } from './gender.model';

export enum GeneralStatus {
  ACTIVE = 'ACTIVE',

  PASSIVE = 'PASSIVE',

  DELETED = 'DELETED',
}
export const GenderLabelMapping: Record<GeneralStatus, string> = {
  [GeneralStatus.ACTIVE]: 'Aktif',
  [GeneralStatus.PASSIVE]: 'Pasif',
  [GeneralStatus.DELETED]: 'Silinmiş',
};
