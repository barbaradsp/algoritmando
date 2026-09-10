import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { ApiService } from '../services/api.service';
import { QuizStateService } from '../services/quiz-state.service';
import { Quiz } from '../models/quiz.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  quizzes: Quiz[] = [];

  carregando: boolean = true;
  erro: string = '';

  constructor(
    private api: ApiService,
    private quizState: QuizStateService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.quizState.reset();

    this.api.listarQuizzes().subscribe({

      next: (quizzes) => {

        this.quizzes = quizzes;
        this.carregando = false;

        this.cdr.detectChanges();
      },

      error: (err) => {

        console.error(
          'Erro ao carregar quizzes:',
          err
        );

        this.erro =
          'Não foi possível carregar os quizzes.';

        this.carregando = false;

        this.cdr.detectChanges();
      }

    });
  }

  selecionarQuiz(quiz: Quiz): void {

    this.quizState.selecionarQuiz(quiz);

    this.router.navigate(['/cadastro']);
  }

  formatarCategoria(
    categoria: string
  ): string {

    const nomes: Record<string, string> = {

      ESTRUTURAS_DE_DADOS:
        'Estruturas de Dados',

      ALGORITMOS:
        'Algoritmos',

      COMPLEXIDADE:
        'Complexidade'
    };

    return nomes[categoria] ?? categoria;
  }

  formatarDificuldade(
    dificuldade: string
  ): string {

    const nomes: Record<string, string> = {

      FACIL:
        'Fácil',

      MEDIO:
        'Médio',

      DIFICIL:
        'Difícil'
    };

    return nomes[dificuldade] ?? dificuldade;
  }
}
