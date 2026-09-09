import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Pontuacao } from './pontuacao';

describe('Pontuacao', () => {
  let component: Pontuacao;
  let fixture: ComponentFixture<Pontuacao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Pontuacao],
    }).compileComponents();

    fixture = TestBed.createComponent(Pontuacao);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
