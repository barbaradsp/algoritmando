import {
  ChangeDetectorRef,
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
  ApiService
} from '../services/api.service';

import {
  QuizStateService
} from '../services/quiz-state.service';

import {
  ResultadoResponse
} from '../models/resultado.model';


@Component({
  selector: 'app-pontuacao',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl:
    './pontuacao.html',

  styleUrl:
    './pontuacao.css'
})
export class Pontuacao
  implements OnInit {

  resultado:
    ResultadoResponse | null = null;

  carregando:
    boolean = true;

  erro:
    string = '';


  constructor(

    private api:
    ApiService,

    private quizState:
    QuizStateService,

    private router:
    Router,

    private cdr:
    ChangeDetectorRef

  ) {}


  ngOnInit(): void {

    if (
      this.quizState.resultadoAtual
    ) {

      this.resultado =
        this.quizState.resultadoAtual;

      this.carregando =
        false;

      return;
    }


    if (
      !this.quizState.usuarioId ||
      !this.quizState.quizId ||
      this.quizState.respostas.length === 0
    ) {

      this.router.navigate([
        '/'
      ]);

      return;
    }


    this.api
      .enviarResultado({

        usuarioId:
        this.quizState.usuarioId,

        quizId:
        this.quizState.quizId,

        respostas:
        this.quizState.respostas

      })
      .subscribe({

        next: (
          resultado
        ) => {

          this.resultado =
            resultado;

          this.quizState
            .salvarResultado(
              resultado
            );

          this.carregando =
            false;

          this.cdr
            .detectChanges();
        },


        error: (
          err
        ) => {

          console.error(
            'Erro ao calcular resultado:',
            err
          );

          this.erro =
            'Erro ao calcular resultado.';

          this.carregando =
            false;

          this.cdr
            .detectChanges();
        }

      });
  }


  revisarRespostas(): void {

    this.router.navigate([
      '/revisao'
    ]);
  }


  verHistorico(): void {

    this.router.navigate([
      '/historico'
    ]);
  }


  irParaFinal(): void {

    this.router.navigate([
      '/final'
    ]);
  }
}
