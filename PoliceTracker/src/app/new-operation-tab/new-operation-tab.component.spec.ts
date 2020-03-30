import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewOperationTabComponent } from './new-operation-tab.component';

describe('NewOperationTabComponent', () => {
  let component: NewOperationTabComponent;
  let fixture: ComponentFixture<NewOperationTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewOperationTabComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewOperationTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
