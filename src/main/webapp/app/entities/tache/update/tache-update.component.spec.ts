jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TacheService } from '../service/tache.service';
import { ITache, Tache } from '../tache.model';
import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';

import { TacheUpdateComponent } from './tache-update.component';

describe('Component Tests', () => {
  describe('Tache Management Update Component', () => {
    let comp: TacheUpdateComponent;
    let fixture: ComponentFixture<TacheUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tacheService: TacheService;
    let agentService: AgentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TacheUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TacheUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TacheUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tacheService = TestBed.inject(TacheService);
      agentService = TestBed.inject(AgentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Agent query and add missing value', () => {
        const tache: ITache = { id: 456 };
        const agent: IAgent = { id: 38843 };
        tache.agent = agent;

        const agentCollection: IAgent[] = [{ id: 48433 }];
        spyOn(agentService, 'query').and.returnValue(of(new HttpResponse({ body: agentCollection })));
        const additionalAgents = [agent];
        const expectedCollection: IAgent[] = [...additionalAgents, ...agentCollection];
        spyOn(agentService, 'addAgentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tache });
        comp.ngOnInit();

        expect(agentService.query).toHaveBeenCalled();
        expect(agentService.addAgentToCollectionIfMissing).toHaveBeenCalledWith(agentCollection, ...additionalAgents);
        expect(comp.agentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tache: ITache = { id: 456 };
        const agent: IAgent = { id: 83549 };
        tache.agent = agent;

        activatedRoute.data = of({ tache });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tache));
        expect(comp.agentsSharedCollection).toContain(agent);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tache = { id: 123 };
        spyOn(tacheService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tache });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tache }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tacheService.update).toHaveBeenCalledWith(tache);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tache = new Tache();
        spyOn(tacheService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tache });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tache }));
        saveSubject.complete();

        // THEN
        expect(tacheService.create).toHaveBeenCalledWith(tache);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tache = { id: 123 };
        spyOn(tacheService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tache });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tacheService.update).toHaveBeenCalledWith(tache);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAgentById', () => {
        it('Should return tracked Agent primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAgentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
