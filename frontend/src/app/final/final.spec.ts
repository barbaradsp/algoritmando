import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Final } from './final';

describe('Final', () => {
  let component: Final;
  let fixture: ComponentFixture<Final>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Final],
    }).compileComponents();

    fixture = TestBed.createComponent(Final);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
