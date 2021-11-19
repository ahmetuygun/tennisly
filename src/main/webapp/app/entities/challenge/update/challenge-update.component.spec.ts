jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChallengeService } from '../service/challenge.service';
import { IChallenge, Challenge } from '../challenge.model';
import { ICord } from 'app/entities/cord/cord.model';
import { CordService } from 'app/entities/cord/service/cord.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';

import { ChallengeUpdateComponent } from './challenge-update.component';

describe('Challenge Management Update Component', () => {
  let comp: ChallengeUpdateComponent;
  let fixture: ComponentFixture<ChallengeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let challengeService: ChallengeService;
  let cordService: CordService;
  let playerService: PlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ChallengeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ChallengeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChallengeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    challengeService = TestBed.inject(ChallengeService);
    cordService = TestBed.inject(CordService);
    playerService = TestBed.inject(PlayerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call cord query and add missing value', () => {
      const challenge: IChallenge = { id: 456 };
      const cord: ICord = { id: 85256 };
      challenge.cord = cord;

      const cordCollection: ICord[] = [{ id: 29003 }];
      jest.spyOn(cordService, 'query').mockReturnValue(of(new HttpResponse({ body: cordCollection })));
      const expectedCollection: ICord[] = [cord, ...cordCollection];
      jest.spyOn(cordService, 'addCordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ challenge });
      comp.ngOnInit();

      expect(cordService.query).toHaveBeenCalled();
      expect(cordService.addCordToCollectionIfMissing).toHaveBeenCalledWith(cordCollection, cord);
      expect(comp.cordsCollection).toEqual(expectedCollection);
    });

    it('Should call Player query and add missing value', () => {
      const challenge: IChallenge = { id: 456 };
      const proposer: IPlayer = { id: 31941 };
      challenge.proposer = proposer;
      const acceptor: IPlayer = { id: 49803 };
      challenge.acceptor = acceptor;

      const playerCollection: IPlayer[] = [{ id: 79293 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [proposer, acceptor];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ challenge });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(playerCollection, ...additionalPlayers);
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const challenge: IChallenge = { id: 456 };
      const cord: ICord = { id: 33533 };
      challenge.cord = cord;
      const proposer: IPlayer = { id: 97049 };
      challenge.proposer = proposer;
      const acceptor: IPlayer = { id: 44851 };
      challenge.acceptor = acceptor;

      activatedRoute.data = of({ challenge });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(challenge));
      expect(comp.cordsCollection).toContain(cord);
      expect(comp.playersSharedCollection).toContain(proposer);
      expect(comp.playersSharedCollection).toContain(acceptor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Challenge>>();
      const challenge = { id: 123 };
      jest.spyOn(challengeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ challenge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: challenge }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(challengeService.update).toHaveBeenCalledWith(challenge);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Challenge>>();
      const challenge = new Challenge();
      jest.spyOn(challengeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ challenge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: challenge }));
      saveSubject.complete();

      // THEN
      expect(challengeService.create).toHaveBeenCalledWith(challenge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Challenge>>();
      const challenge = { id: 123 };
      jest.spyOn(challengeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ challenge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(challengeService.update).toHaveBeenCalledWith(challenge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCordById', () => {
      it('Should return tracked Cord primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCordById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlayerById', () => {
      it('Should return tracked Player primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
