jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CordService } from '../service/cord.service';
import { ICord, Cord } from '../cord.model';

import { CordUpdateComponent } from './cord-update.component';

describe('Cord Management Update Component', () => {
  let comp: CordUpdateComponent;
  let fixture: ComponentFixture<CordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cordService: CordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CordUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cordService = TestBed.inject(CordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cord: ICord = { id: 456 };

      activatedRoute.data = of({ cord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cord));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cord>>();
      const cord = { id: 123 };
      jest.spyOn(cordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cordService.update).toHaveBeenCalledWith(cord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cord>>();
      const cord = new Cord();
      jest.spyOn(cordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cord }));
      saveSubject.complete();

      // THEN
      expect(cordService.create).toHaveBeenCalledWith(cord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cord>>();
      const cord = { id: 123 };
      jest.spyOn(cordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cordService.update).toHaveBeenCalledWith(cord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
