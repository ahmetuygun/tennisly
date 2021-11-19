import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cord',
        data: { pageTitle: 'Cords' },
        loadChildren: () => import('./cord/cord.module').then(m => m.CordModule),
      },
      {
        path: 'player',
        data: { pageTitle: 'Players' },
        loadChildren: () => import('./player/player.module').then(m => m.PlayerModule),
      },
      {
        path: 'challenge',
        data: { pageTitle: 'Challenges' },
        loadChildren: () => import('./challenge/challenge.module').then(m => m.ChallengeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
