import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMateriel, Materiel } from '../materiel.model';
import { MaterielService } from '../service/materiel.service';
import { ITache } from 'app/entities/tache/tache.model';
import { TacheService } from 'app/entities/tache/service/tache.service';

@Component({
  selector: 'jhi-materiel-update',
  templateUrl: './materiel-update.component.html',
})
export class MaterielUpdateComponent implements OnInit {
  isSaving = false;

  tachesSharedCollection: ITache[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    quantityUse: [],
    quantityStock: [],
    tache: [],
  });

  constructor(
    protected materielService: MaterielService,
    protected tacheService: TacheService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materiel }) => {
      this.updateForm(materiel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materiel = this.createFromForm();
    if (materiel.id !== undefined) {
      this.subscribeToSaveResponse(this.materielService.update(materiel));
    } else {
      this.subscribeToSaveResponse(this.materielService.create(materiel));
    }
  }

  trackTacheById(index: number, item: ITache): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMateriel>>): void {
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

  protected updateForm(materiel: IMateriel): void {
    this.editForm.patchValue({
      id: materiel.id,
      libelle: materiel.libelle,
      quantityUse: materiel.quantityUse,
      quantityStock: materiel.quantityStock,
      tache: materiel.tache,
    });

    this.tachesSharedCollection = this.tacheService.addTacheToCollectionIfMissing(this.tachesSharedCollection, materiel.tache);
  }

  protected loadRelationshipsOptions(): void {
    this.tacheService
      .query()
      .pipe(map((res: HttpResponse<ITache[]>) => res.body ?? []))
      .pipe(map((taches: ITache[]) => this.tacheService.addTacheToCollectionIfMissing(taches, this.editForm.get('tache')!.value)))
      .subscribe((taches: ITache[]) => (this.tachesSharedCollection = taches));
  }

  protected createFromForm(): IMateriel {
    return {
      ...new Materiel(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      quantityUse: this.editForm.get(['quantityUse'])!.value,
      quantityStock: this.editForm.get(['quantityStock'])!.value,
      tache: this.editForm.get(['tache'])!.value,
    };
  }
}
