import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewestTabComponent } from './newest-tab.component';

describe('NewestTabComponent', () => {
  let component: NewestTabComponent;
  let fixture: ComponentFixture<NewestTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewestTabComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewestTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
