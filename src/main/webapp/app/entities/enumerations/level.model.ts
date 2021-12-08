import { Gender } from './gender.model';

export enum Level {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED',
  PROFICIENT = 'PROFICIENT',
}

export const LevelLabelMapping: Record<Level, string> = {
  [Level.BEGINNER]: 'Başlangıç',
  [Level.INTERMEDIATE]: 'Orta',
  [Level.ADVANCED]: 'İyi',
  [Level.PROFICIENT]: 'Profesyonel',
};
