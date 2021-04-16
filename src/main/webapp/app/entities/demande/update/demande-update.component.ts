import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDemande, Demande } from '../demande.model';
import { DemandeService } from '../service/demande.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-demande-update',
  templateUrl: './demande-update.component.html',
})
export class DemandeUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    statut: [],
    dateDemande: [],
    lieu: [],
    duree: [],
    priorite: [],
    causeDefaillance: [],
    autresCauses: [],
    departement: [],
    typeDefaillance: [],
    client: [],
  });

  constructor(
    protected demandeService: DemandeService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demande }) => {
      if (demande.id === undefined) {
        const today = dayjs().startOf('day');
        demande.dateDemande = today;
      }

      this.updateForm(demande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demande = this.createFromForm();
    if (demande.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeService.update(demande));
    } else {
      this.subscribeToSaveResponse(this.demandeService.create(demande));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemande>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(demande: IDemande): void {
    this.editForm.patchValue({
      id: demande.id,
      statut: demande.statut,
      dateDemande: demande.dateDemande ? demande.dateDemande.format(DATE_TIME_FORMAT) : null,
      lieu: demande.lieu,
      duree: demande.duree,
      priorite: demande.priorite,
      causeDefaillance: demande.causeDefaillance,
      autresCauses: demande.autresCauses,
      departement: demande.departement,
      typeDefaillance: demande.typeDefaillance,
      client: demande.client,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, demande.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): IDemande {
    return {
      ...new Demande(),
      id: this.editForm.get(['id'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      dateDemande: this.editForm.get(['dateDemande'])!.value
        ? dayjs(this.editForm.get(['dateDemande'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lieu: this.editForm.get(['lieu'])!.value,
      duree: this.editForm.get(['duree'])!.value,
      priorite: this.editForm.get(['priorite'])!.value,
      causeDefaillance: this.editForm.get(['causeDefaillance'])!.value,
      autresCauses: this.editForm.get(['autresCauses'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      typeDefaillance: this.editForm.get(['typeDefaillance'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
