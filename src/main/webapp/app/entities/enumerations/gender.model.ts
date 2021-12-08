export enum Gender {
  MAN = 'MAN',

  WOMEN = 'WOMEN',
}

export const GenderLabelMapping: Record<Gender, string> = {
  [Gender.MAN]: 'Erkek',
  [Gender.WOMEN]: 'Kadın',
};
