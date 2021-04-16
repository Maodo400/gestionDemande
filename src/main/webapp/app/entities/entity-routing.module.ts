import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'agent',
        data: { pageTitle: 'gestionDemandeApp.agent.home.title' },
        loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule),
      },
      {
        path: 'chef-service',
        data: { pageTitle: 'gestionDemandeApp.chefService.home.title' },
        loadChildren: () => import('./chef-service/chef-service.module').then(m => m.ChefServiceModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'gestionDemandeApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'demande',
        data: { pageTitle: 'gestionDemandeApp.demande.home.title' },
        loadChildren: () => import('./demande/demande.module').then(m => m.DemandeModule),
      },
      {
        path: 'tache',
        data: { pageTitle: 'gestionDemandeApp.tache.home.title' },
        loadChildren: () => import('./tache/tache.module').then(m => m.TacheModule),
      },
      {
        path: 'materiel',
        data: { pageTitle: 'gestionDemandeApp.materiel.home.title' },
        loadChildren: () => import('./materiel/materiel.module').then(m => m.MaterielModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
