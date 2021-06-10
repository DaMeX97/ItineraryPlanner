import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAttractionModalComponent } from './add-attraction-modal.component';

describe('AddAttractionModalComponent', () => {
  let component: AddAttractionModalComponent;
  let fixture: ComponentFixture<AddAttractionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddAttractionModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddAttractionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
