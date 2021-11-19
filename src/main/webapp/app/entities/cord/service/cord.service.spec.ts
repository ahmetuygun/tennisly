import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { GeneralStatus } from 'app/entities/enumerations/general-status.model';
import { ICord, Cord } from '../cord.model';

import { CordService } from './cord.service';

describe('Cord Service', () => {
  let service: CordService;
  let httpMock: HttpTestingController;
  let elemDefault: ICord;
  let expectedResult: ICord | ICord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      adress: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
      status: GeneralStatus.ACTIVE,
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

    it('should create a Cord', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          adress: 'BBBBBB',
          image: 'BBBBBB',
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cord', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          image: 'BBBBBB',
        },
        new Cord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          adress: 'BBBBBB',
          image: 'BBBBBB',
          status: 'BBBBBB',
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

    it('should delete a Cord', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCordToCollectionIfMissing', () => {
      it('should add a Cord to an empty array', () => {
        const cord: ICord = { id: 123 };
        expectedResult = service.addCordToCollectionIfMissing([], cord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cord);
      });

      it('should not add a Cord to an array that contains it', () => {
        const cord: ICord = { id: 123 };
        const cordCollection: ICord[] = [
          {
            ...cord,
          },
          { id: 456 },
        ];
        expectedResult = service.addCordToCollectionIfMissing(cordCollection, cord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cord to an array that doesn't contain it", () => {
        const cord: ICord = { id: 123 };
        const cordCollection: ICord[] = [{ id: 456 }];
        expectedResult = service.addCordToCollectionIfMissing(cordCollection, cord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cord);
      });

      it('should add only unique Cord to an array', () => {
        const cordArray: ICord[] = [{ id: 123 }, { id: 456 }, { id: 81753 }];
        const cordCollection: ICord[] = [{ id: 123 }];
        expectedResult = service.addCordToCollectionIfMissing(cordCollection, ...cordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cord: ICord = { id: 123 };
        const cord2: ICord = { id: 456 };
        expectedResult = service.addCordToCollectionIfMissing([], cord, cord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cord);
        expect(expectedResult).toContain(cord2);
      });

      it('should accept null and undefined values', () => {
        const cord: ICord = { id: 123 };
        expectedResult = service.addCordToCollectionIfMissing([], null, cord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cord);
      });

      it('should return initial array if no Cord is added', () => {
        const cordCollection: ICord[] = [{ id: 123 }];
        expectedResult = service.addCordToCollectionIfMissing(cordCollection, undefined, null);
        expect(expectedResult).toEqual(cordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
