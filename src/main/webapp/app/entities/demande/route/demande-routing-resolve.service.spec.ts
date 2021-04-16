jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDemande, Demande } from '../demande.model';
import { DemandeService } from '../service/demande.service';

import { DemandeRoutingResolveService } from './demande-routing-resolve.service';

describe('Service Tests', () => {
  describe('Demande routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DemandeRoutingResolveService;
    let service: DemandeService;
    let resultDemande: IDemande | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DemandeRoutingResolveService);
      service = TestBed.inject(DemandeService);
      resultDemande = undefined;
    });

    describe('resolve', () => {
      it('should return IDemande returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemande).toEqual({ id: 123 });
      });

      it('should return new IDemande if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemande = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDemande).toEqual(new Demande());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemande).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
