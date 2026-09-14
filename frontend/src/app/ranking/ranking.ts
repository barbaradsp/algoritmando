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
  RankingResultado
} from '../models/ranking.model';

@Component({
  selector: 'app-ranking',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl:
    './ranking.html',

  styleUrl:
    './ranking.css'
})
export class Ranking
  implements OnInit {

  ranking:
    RankingResultado[] = [];

  carregando:
    boolean = true;

  erro:
    string = '';

  constructor(

    private api:
    ApiService,

    public quizState:
    QuizStateService,

    private router:
    Router,

    private cdr:
    ChangeDetectorRef

  ) {}

  ngOnInit(): void {

    if (
      !this.quizState.quizId
    ) {

      this.router.navigate([
        '/'
      ]);

      return;
    }

    this.api
      .listarRanking(
        this.quizState.quizId
      )
      .subscribe({

        next: (
          ranking
        ) => {

          this.ranking =
            ranking;

          this.carregando =
            false;

          this.cdr
            .detectChanges();
        },

        error: (
          err
        ) => {

          console.error(
            'Erro ao carregar ranking:',
            err
          );

          this.erro =
            'Não foi possível carregar o ranking.';

          this.carregando =
            false;

          this.cdr
            .detectChanges();
        }

      });
  }

  ehUsuarioAtual(
    resultado: RankingResultado
  ): boolean {

    return (
      resultado.usuarioId ===
      this.quizState.usuarioId
    );
  }

  exibirPosicao(
    posicao: number
  ): string {

    switch (posicao) {

      case 1:
        return '🥇';

      case 2:
        return '🥈';

      case 3:
        return '🥉';

      default:
        return `${posicao}º`;
    }
  }

  voltarResultado(): void {

    this.router.navigate([
      '/pontuacao'
    ]);
  }
}
