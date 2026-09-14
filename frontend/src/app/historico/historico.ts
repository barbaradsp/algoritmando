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
  forkJoin
} from 'rxjs';

import {
  ApiService
} from '../services/api.service';

import {
  QuizStateService
} from '../services/quiz-state.service';

import {
  DesempenhoUsuario,
  HistoricoResultado
} from '../models/resultado.model';


@Component({
  selector: 'app-historico',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl:
    './historico.html',

  styleUrl:
    './historico.css'
})
export class Historico
  implements OnInit {

  resultados:
    HistoricoResultado[] = [];

  desempenho:
    DesempenhoUsuario | null = null;

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
      !this.quizState.emailUsuario
    ) {

      this.router.navigate([
        '/'
      ]);

      return;
    }


    forkJoin({

      historico:
        this.api.listarHistorico(
          this.quizState.emailUsuario
        ),

      desempenho:
        this.api.obterDesempenho(
          this.quizState.emailUsuario
        )

    })
      .subscribe({

        next: (
          resposta
        ) => {

          this.resultados =
            resposta.historico;

          this.desempenho =
            resposta.desempenho;

          this.carregando =
            false;

          this.cdr
            .detectChanges();
        },


        error: (
          err
        ) => {

          console.error(
            'Erro ao carregar histórico:',
            err
          );

          this.erro =
            'Não foi possível carregar seu histórico.';

          this.carregando =
            false;

          this.cdr
            .detectChanges();
        }

      });
  }


  formatarCategoria(
    categoria: string
  ): string {

    const categorias:
      Record<string, string> = {

      ESTRUTURAS_DE_DADOS:
        'Estruturas de Dados',

      ALGORITMOS:
        'Algoritmos',

      COMPLEXIDADE:
        'Complexidade'
    };

    return (
      categorias[categoria] ??
      categoria
    );
  }


  formatarDificuldade(
    dificuldade: string
  ): string {

    const dificuldades:
      Record<string, string> = {

      FACIL:
        'Fácil',

      MEDIO:
        'Médio',

      DIFICIL:
        'Difícil'
    };

    return (
      dificuldades[dificuldade] ??
      dificuldade
    );
  }


  formatarData(
    data: string
  ): string {

    return new Date(
      data
    ).toLocaleString(
      'pt-BR',
      {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',

        hour: '2-digit',
        minute: '2-digit'
      }
    );
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
