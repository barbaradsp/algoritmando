import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef
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
  changeDetection: ChangeDetectionStrategy.Eager
})
export class Quiz implements OnInit {
  perguntas: Pergunta[] = [];
  indiceAtual: number = 0;
  carregando: boolean = true;
  erro: string = '';
  alternativaSelecionada: string | null = null;

  constructor(
    private api: ApiService,
    private quizState: QuizStateService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    console.log('1 - Quiz iniciou');
    console.log('2 - Nome do usuário:', this.quizState.nomeUsuario);

    if (!this.quizState.nomeUsuario) {
      console.log('3 - Usuário sem nome. Redirecionando para home.');
      this.router.navigate(['/']);
      return;
    }

    console.log('3 - Vou chamar GET /perguntas');

    this.api.listarPerguntas().subscribe({
      next: (perguntas) => {
        console.log('4 - Entrou no NEXT');
        console.log('5 - Resposta recebida:', perguntas);
        console.log('6 - É um array?', Array.isArray(perguntas));
        console.log('7 - Quantidade:', perguntas?.length);

        if (!Array.isArray(perguntas)) {
          console.error('ERRO: backend não retornou um array');

          this.perguntas = [];
          this.erro = 'O backend retornou as perguntas em um formato inesperado.';
          this.carregando = false;

          return;
        }

        this.perguntas = perguntas;

        console.log('8 - Perguntas salvas:', this.perguntas);

        this.carregando = false;

        console.log('9 - carregando agora vale:', this.carregando);

        this.cdr.detectChanges();

        console.log('10 - detectChanges executado');
      },

      error: (err) => {
        console.error('ERRO NA REQUISIÇÃO:', err);

        this.erro = 'Erro ao carregar perguntas.';
        this.carregando = false;

        this.cdr.detectChanges();
      },

      complete: () => {
        console.log('10 - Requisição finalizada');
      }
    });
  }

  get perguntaAtual(): Pergunta | null {
    if (!Array.isArray(this.perguntas)) {
      return null;
    }

    return this.perguntas[this.indiceAtual] ?? null;
  }

  get progresso(): number {
    if (this.perguntas.length === 0) return 0;
    return ((this.indiceAtual + 1) / this.perguntas.length) * 100;
  }

  selecionar(alternativa: string): void {
    this.alternativaSelecionada = alternativa;
  }

  proxima(): void {
    if (!this.alternativaSelecionada || !this.perguntaAtual) return;

    this.quizState.adicionarResposta({
      perguntaId: this.perguntaAtual.id,
      respostaEscolhida: this.alternativaSelecionada
    });

    this.alternativaSelecionada = null;

    if (this.indiceAtual < this.perguntas.length - 1) {
      this.indiceAtual++;
    } else {
      this.finalizar();
    }
  }

  finalizar(): void {
    this.router.navigate(['/pontuacao']);
  }
}
