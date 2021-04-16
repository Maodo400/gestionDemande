jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MaterielService } from '../service/materiel.service';
import { IMateriel, Materiel } from '../materiel.model';
import { ITache } from 'app/entities/tache/tache.model';
import { TacheService } from 'app/entities/tache/service/tache.service';

import { MaterielUpdateComponent } from './materiel-update.component';

describe('Component Tests', () => {
  describe('Materiel Management Update Component', () => {
    let comp: MaterielUpdateComponent;
    let fixture: ComponentFixture<MaterielUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let materielService: MaterielService;
    let tacheService: TacheService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MaterielUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MaterielUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MaterielUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      materielService = TestBed.inject(MaterielService);
      tacheService = TestBed.inject(TacheService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Tache query and add missing value', () => {
        const materiel: IMateriel = { id: 456 };
        const tache: ITache = { id: 84215 };
        materiel.tache = tache;

        const tacheCollection: ITache[] = [{ id: 82071 }];
        spyOn(tacheService, 'query').and.returnValue(of(new HttpResponse({ body: tacheCollection })));
        const additionalTaches = [tache];
        const expectedCollection: ITache[] = [...additionalTaches, ...tacheCollection];
        spyOn(tacheService, 'addTacheToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ materiel });
        comp.ngOnInit();

        expect(tacheService.query).toHaveBeenCalled();
        expect(tacheService.addTacheToCollectionIfMissing).toHaveBeenCalledWith(tacheCollection, ...additionalTaches);
        expect(comp.tachesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const materiel: IMateriel = { id: 456 };
        const tache: ITache = { id: 84937 };
        materiel.tache = tache;

        activatedRoute.data = of({ materiel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(materiel));
        expect(comp.tachesSharedCollection).toContain(tache);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const materiel = { id: 123 };
        spyOn(materielService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ materiel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: materiel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(materielService.update).toHaveBeenCalledWith(materiel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const materiel = new Materiel();
        spyOn(materielService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ materiel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: materiel }));
        saveSubject.complete();

        // THEN
        expect(materielService.create).toHaveBeenCalledWith(materiel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const materiel = { id: 123 };
        spyOn(materielService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ materiel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(materielService.update).toHaveBeenCalledWith(materiel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTacheById', () => {
        it('Should return tracked Tache primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTacheById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
