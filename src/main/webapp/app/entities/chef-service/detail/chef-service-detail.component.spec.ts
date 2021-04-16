import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChefServiceDetailComponent } from './chef-service-detail.component';

describe('Component Tests', () => {
  describe('ChefService Management Detail Component', () => {
    let comp: ChefServiceDetailComponent;
    let fixture: ComponentFixture<ChefServiceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChefServiceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chefService: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChefServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChefServiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chefService on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chefService).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
