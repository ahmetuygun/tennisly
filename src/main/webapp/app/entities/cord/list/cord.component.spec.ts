import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CordService } from '../service/cord.service';

import { CordComponent } from './cord.component';

describe('Cord Management Component', () => {
  let comp: CordComponent;
  let fixture: ComponentFixture<CordComponent>;
  let service: CordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CordComponent],
    })
      .overrideTemplate(CordComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CordComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CordService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.cords?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
