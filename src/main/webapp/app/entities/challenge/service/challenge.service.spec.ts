import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ChallengeStatus } from 'app/entities/enumerations/challenge-status.model';
import { GeneralStatus } from 'app/entities/enumerations/general-status.model';
import { IChallenge, Challenge } from '../challenge.model';

import { ChallengeService } from './challenge.service';

describe('Challenge Service', () => {
  let service: ChallengeService;
  let httpMock: HttpTestingController;
  let elemDefault: IChallenge;
  let expectedResult: IChallenge | IChallenge[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChallengeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      matchTime: currentDate,
      challengeStatus: ChallengeStatus.REQUESTED,
      status: GeneralStatus.ACTIVE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          matchTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Challenge', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          matchTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          matchTime: currentDate,
        },
        returnedFromService
      );

      service.create(new Challenge()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Challenge', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          matchTime: currentDate.format(DATE_TIME_FORMAT),
          challengeStatus: 'BBBBBB',
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          matchTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Challenge', () => {
      const patchObject = Object.assign(
        {
          matchTime: currentDate.format(DATE_TIME_FORMAT),
        },
        new Challenge()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          matchTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Challenge', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          matchTime: currentDate.format(DATE_TIME_FORMAT),
          challengeStatus: 'BBBBBB',
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          matchTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Challenge', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChallengeToCollectionIfMissing', () => {
      it('should add a Challenge to an empty array', () => {
        const challenge: IChallenge = { id: 123 };
        expectedResult = service.addChallengeToCollectionIfMissing([], challenge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(challenge);
      });

      it('should not add a Challenge to an array that contains it', () => {
        const challenge: IChallenge = { id: 123 };
        const challengeCollection: IChallenge[] = [
          {
            ...challenge,
          },
          { id: 456 },
        ];
        expectedResult = service.addChallengeToCollectionIfMissing(challengeCollection, challenge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Challenge to an array that doesn't contain it", () => {
        const challenge: IChallenge = { id: 123 };
        const challengeCollection: IChallenge[] = [{ id: 456 }];
        expectedResult = service.addChallengeToCollectionIfMissing(challengeCollection, challenge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(challenge);
      });

      it('should add only unique Challenge to an array', () => {
        const challengeArray: IChallenge[] = [{ id: 123 }, { id: 456 }, { id: 20979 }];
        const challengeCollection: IChallenge[] = [{ id: 123 }];
        expectedResult = service.addChallengeToCollectionIfMissing(challengeCollection, ...challengeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const challenge: IChallenge = { id: 123 };
        const challenge2: IChallenge = { id: 456 };
        expectedResult = service.addChallengeToCollectionIfMissing([], challenge, challenge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(challenge);
        expect(expectedResult).toContain(challenge2);
      });

      it('should accept null and undefined values', () => {
        const challenge: IChallenge = { id: 123 };
        expectedResult = service.addChallengeToCollectionIfMissing([], null, challenge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(challenge);
      });

      it('should return initial array if no Challenge is added', () => {
        const challengeCollection: IChallenge[] = [{ id: 123 }];
        expectedResult = service.addChallengeToCollectionIfMissing(challengeCollection, undefined, null);
        expect(expectedResult).toEqual(challengeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
