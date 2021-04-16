import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemande } from '../demande.model';

@Component({
  selector: 'jhi-demande-detail',
  templateUrl: './demande-detail.component.html',
})
export class DemandeDetailComponent implements OnInit {
  demande: IDemande | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demande }) => {
      this.demande = demande;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
