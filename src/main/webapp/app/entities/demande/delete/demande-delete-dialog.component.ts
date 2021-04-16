import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemande } from '../demande.model';
import { DemandeService } from '../service/demande.service';

@Component({
  templateUrl: './demande-delete-dialog.component.html',
})
export class DemandeDeleteDialogComponent {
  demande?: IDemande;

  constructor(protected demandeService: DemandeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
