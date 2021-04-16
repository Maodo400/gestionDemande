jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DemandeService } from '../service/demande.service';
import { IDemande, Demande } from '../demande.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { DemandeUpdateComponent } from './demande-update.component';

describe('Component Tests', () => {
  describe('Demande Management Update Component', () => {
    let comp: DemandeUpdateComponent;
    let fixture: ComponentFixture<DemandeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let demandeService: DemandeService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemandeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DemandeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemandeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      demandeService = TestBed.inject(DemandeService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const demande: IDemande = { id: 456 };
        const client: IClient = { id: 52291 };
        demande.client = client;

        const clientCollection: IClient[] = [{ id: 47270 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ demande });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const demande: IDemande = { id: 456 };
        const client: IClient = { id: 76676 };
        demande.client = client;

        activatedRoute.data = of({ demande });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(demande));
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const demande = { id: 123 };
        spyOn(demandeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ demande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demande }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(demandeService.update).toHaveBeenCalledWith(demande);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const demande = new Demande();
        spyOn(demandeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ demande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demande }));
        saveSubject.complete();

        // THEN
        expect(demandeService.create).toHaveBeenCalledWith(demande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const demande = { id: 123 };
        spyOn(demandeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ demande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(demandeService.update).toHaveBeenCalledWith(demande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
