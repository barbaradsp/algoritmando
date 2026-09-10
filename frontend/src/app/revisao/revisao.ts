import {
  Component,
  OnInit
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  Router
} from '@angular/router';

import {
  ResultadoResponse
} from '../models/resultado.model';

import {
  QuizStateService
} from '../services/quiz-state.service';

@Component({
  selector: 'app-revisao',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './revisao.html',
  styleUrl: './revisao.css'
})
export class Revisao
  implements OnInit {

  resultado:
    ResultadoResponse | null = null;

  constructor(
    private quizState:
    QuizStateService,

    private router:
    Router
  ) {}

  ngOnInit(): void {

    this.resultado =
      this.quizState
        .resultadoAtual;

    if (!this.resultado) {

      this.router.navigate([
        '/'
      ]);
    }
  }

  voltarResultado(): void {

    this.router.navigate([
      '/pontuacao'
    ]);
  }

  irParaFinal(): void {

    this.router.navigate([
      '/final'
    ]);
  }
}
