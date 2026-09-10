import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { ApiService } from '../services/api.service';
import { QuizStateService } from '../services/quiz-state.service';
import { Pergunta } from '../models/pergunta.model';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './quiz.html',
  styleUrl: './quiz.css',
  changeDetection:
  ChangeDetectionStrategy.Eager
})
export class Quiz implements OnInit {

  perguntas: Pergunta[] = [];

  indiceAtual: number = 0;

  carregando: boolean = true;

  erro: string = '';

  alternativaSelecionada:
    string | null = null;

  constructor(
    private api: ApiService,
    public quizState: QuizStateService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    if (
      !this.quizState.nomeUsuario ||
      !this.quizState.quizId
    ) {
      this.router.navigate(['/']);
      return;
    }

    const quizId =
      this.quizState.quizId;

    this.api
      .listarPerguntasPorQuiz(quizId)
      .subscribe({

        next: (perguntas) => {

          if (
            !Array.isArray(perguntas) ||
            perguntas.length === 0
          ) {

            this.erro =
              'Este quiz ainda não possui perguntas.';

            this.carregando = false;

            this.cdr.detectChanges();

            return;
          }

          this.perguntas = perguntas;

          this.carregando = false;

          this.cdr.detectChanges();
        },

        error: (err) => {

          console.error(
            'Erro ao buscar perguntas:',
            err
          );

          this.erro =
            'Erro ao carregar perguntas.';

          this.carregando = false;

          this.cdr.detectChanges();
        }

      });
  }

  get perguntaAtual():
    Pergunta | null {

    return this.perguntas[
      this.indiceAtual
      ] ?? null;
  }

  get progresso(): number {

    if (
      this.perguntas.length === 0
    ) {
      return 0;
    }

    return (
      (
        this.indiceAtual + 1
      ) /
      this.perguntas.length
    ) * 100;
  }

  selecionar(
    alternativa: string
  ): void {

    this.alternativaSelecionada =
      alternativa;
  }

  proxima(): void {

    if (
      !this.alternativaSelecionada ||
      !this.perguntaAtual
    ) {
      return;
    }

    this.quizState
      .adicionarResposta({

        perguntaId:
        this.perguntaAtual.id,

        respostaEscolhida:
        this.alternativaSelecionada
      });

    this.alternativaSelecionada =
      null;

    if (
      this.indiceAtual <
      this.perguntas.length - 1
    ) {

      this.indiceAtual++;

    } else {

      this.finalizar();
    }
  }

  finalizar(): void {

    this.router.navigate([
      '/pontuacao'
    ]);
  }
}
