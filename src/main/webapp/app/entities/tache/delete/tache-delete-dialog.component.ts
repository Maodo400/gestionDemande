import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITache } from '../tache.model';
import { TacheService } from '../service/tache.service';

@Component({
  templateUrl: './tache-delete-dialog.component.html',
})
export class TacheDeleteDialogComponent {
  tache?: ITache;

  constructor(protected tacheService: TacheService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tacheService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
