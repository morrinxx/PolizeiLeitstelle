import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportsOfOperationComponent } from './reports-of-operation.component';

describe('ReportsOfOperationComponent', () => {
  let component: ReportsOfOperationComponent;
  let fixture: ComponentFixture<ReportsOfOperationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportsOfOperationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportsOfOperationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
