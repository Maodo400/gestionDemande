import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { StatusDemande } from 'app/entities/enumerations/status-demande.model';
import { EnumPriorite } from 'app/entities/enumerations/enum-priorite.model';
import { EnumCause } from 'app/entities/enumerations/enum-cause.model';
import { EnumDepartement } from 'app/entities/enumerations/enum-departement.model';
import { EnumService } from 'app/entities/enumerations/enum-service.model';
import { IDemande, Demande } from '../demande.model';

import { DemandeService } from './demande.service';

describe('Service Tests', () => {
  describe('Demande Service', () => {
    let service: DemandeService;
    let httpMock: HttpTestingController;
    let elemDefault: IDemande;
    let expectedResult: IDemande | IDemande[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DemandeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        statut: StatusDemande.Ok,
        dateDemande: currentDate,
        lieu: 'AAAAAAA',
        duree: 0,
        priorite: EnumPriorite.Pas_Urgent,
        causeDefaillance: EnumCause.Usure_Normale,
        autresCauses: 'AAAAAAA',
        departement: EnumDepartement.Genie_Civile,
        typeDefaillance: EnumService.Maconnerie,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDemande: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Demande', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDemande: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDemande: currentDate,
          },
          returnedFromService
        );

        service.create(new Demande()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Demande', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            statut: 'BBBBBB',
            dateDemande: currentDate.format(DATE_TIME_FORMAT),
            lieu: 'BBBBBB',
            duree: 1,
            priorite: 'BBBBBB',
            causeDefaillance: 'BBBBBB',
            autresCauses: 'BBBBBB',
            departement: 'BBBBBB',
            typeDefaillance: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDemande: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Demande', () => {
        const patchObject = Object.assign(
          {
            dateDemande: currentDate.format(DATE_TIME_FORMAT),
            lieu: 'BBBBBB',
            departement: 'BBBBBB',
            typeDefaillance: 'BBBBBB',
          },
          new Demande()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDemande: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Demande', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            statut: 'BBBBBB',
            dateDemande: currentDate.format(DATE_TIME_FORMAT),
            lieu: 'BBBBBB',
            duree: 1,
            priorite: 'BBBBBB',
            causeDefaillance: 'BBBBBB',
            autresCauses: 'BBBBBB',
            departement: 'BBBBBB',
            typeDefaillance: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDemande: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Demande', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDemandeToCollectionIfMissing', () => {
        it('should add a Demande to an empty array', () => {
          const demande: IDemande = { id: 123 };
          expectedResult = service.addDemandeToCollectionIfMissing([], demande);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demande);
        });

        it('should not add a Demande to an array that contains it', () => {
          const demande: IDemande = { id: 123 };
          const demandeCollection: IDemande[] = [
            {
              ...demande,
            },
            { id: 456 },
          ];
          expectedResult = service.addDemandeToCollectionIfMissing(demandeCollection, demande);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Demande to an array that doesn't contain it", () => {
          const demande: IDemande = { id: 123 };
          const demandeCollection: IDemande[] = [{ id: 456 }];
          expectedResult = service.addDemandeToCollectionIfMissing(demandeCollection, demande);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demande);
        });

        it('should add only unique Demande to an array', () => {
          const demandeArray: IDemande[] = [{ id: 123 }, { id: 456 }, { id: 17706 }];
          const demandeCollection: IDemande[] = [{ id: 123 }];
          expectedResult = service.addDemandeToCollectionIfMissing(demandeCollection, ...demandeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const demande: IDemande = { id: 123 };
          const demande2: IDemande = { id: 456 };
          expectedResult = service.addDemandeToCollectionIfMissing([], demande, demande2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demande);
          expect(expectedResult).toContain(demande2);
        });

        it('should accept null and undefined values', () => {
          const demande: IDemande = { id: 123 };
          expectedResult = service.addDemandeToCollectionIfMissing([], null, demande, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demande);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
