import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';
import { IChefService, ChefService } from '../chef-service.model';

import { ChefServiceService } from './chef-service.service';

describe('Service Tests', () => {
  describe('ChefService Service', () => {
    let service: ChefServiceService;
    let httpMock: HttpTestingController;
    let elemDefault: IChefService;
    let expectedResult: IChefService | IChefService[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChefServiceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        firstName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        sexe: EnumSexe.M,
        email: 'AAAAAAA',
        tel: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ChefService', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ChefService()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChefService', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            sexe: 'BBBBBB',
            email: 'BBBBBB',
            tel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ChefService', () => {
        const patchObject = Object.assign(
          {
            firstName: 'BBBBBB',
            sexe: 'BBBBBB',
            tel: 'BBBBBB',
          },
          new ChefService()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ChefService', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            sexe: 'BBBBBB',
            email: 'BBBBBB',
            tel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ChefService', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChefServiceToCollectionIfMissing', () => {
        it('should add a ChefService to an empty array', () => {
          const chefService: IChefService = { id: 123 };
          expectedResult = service.addChefServiceToCollectionIfMissing([], chefService);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefService);
        });

        it('should not add a ChefService to an array that contains it', () => {
          const chefService: IChefService = { id: 123 };
          const chefServiceCollection: IChefService[] = [
            {
              ...chefService,
            },
            { id: 456 },
          ];
          expectedResult = service.addChefServiceToCollectionIfMissing(chefServiceCollection, chefService);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChefService to an array that doesn't contain it", () => {
          const chefService: IChefService = { id: 123 };
          const chefServiceCollection: IChefService[] = [{ id: 456 }];
          expectedResult = service.addChefServiceToCollectionIfMissing(chefServiceCollection, chefService);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefService);
        });

        it('should add only unique ChefService to an array', () => {
          const chefServiceArray: IChefService[] = [{ id: 123 }, { id: 456 }, { id: 2424 }];
          const chefServiceCollection: IChefService[] = [{ id: 123 }];
          expectedResult = service.addChefServiceToCollectionIfMissing(chefServiceCollection, ...chefServiceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chefService: IChefService = { id: 123 };
          const chefService2: IChefService = { id: 456 };
          expectedResult = service.addChefServiceToCollectionIfMissing([], chefService, chefService2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefService);
          expect(expectedResult).toContain(chefService2);
        });

        it('should accept null and undefined values', () => {
          const chefService: IChefService = { id: 123 };
          expectedResult = service.addChefServiceToCollectionIfMissing([], null, chefService, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefService);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
