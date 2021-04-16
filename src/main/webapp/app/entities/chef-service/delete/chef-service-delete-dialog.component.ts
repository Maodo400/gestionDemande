import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChefService } from '../chef-service.model';
import { ChefServiceService } from '../service/chef-service.service';

@Component({
  templateUrl: './chef-service-delete-dialog.component.html',
})
export class ChefServiceDeleteDialogComponent {
  chefService?: IChefService;

  constructor(protected chefServiceService: ChefServiceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chefServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
