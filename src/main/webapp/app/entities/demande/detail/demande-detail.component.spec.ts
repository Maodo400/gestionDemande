import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeDetailComponent } from './demande-detail.component';

describe('Component Tests', () => {
  describe('Demande Management Detail Component', () => {
    let comp: DemandeDetailComponent;
    let fixture: ComponentFixture<DemandeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DemandeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ demande: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DemandeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DemandeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load demande on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.demande).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
