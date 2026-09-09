import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { QuizStateService } from '../services/quiz-state.service';
import { ResultadoResponse } from '../models/resultado.model';

@Component({
  selector: 'app-pontuacao',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pontuacao.html',
  styleUrl: './pontuacao.css'
})
export class Pontuacao implements OnInit {
  resultado: ResultadoResponse | null = null;
  carregando: boolean = true;
  erro: string = '';

  constructor(
    private api: ApiService,
    private quizState: QuizStateService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Se não tem respostas registradas, o usuário pulou o fluxo
    if (!this.quizState.nomeUsuario || this.quizState.respostas.length === 0) {
      this.router.navigate(['/']);
      return;
    }

    this.api.enviarResultado({
      nomeUsuario: this.quizState.nomeUsuario,
      respostas: this.quizState.respostas
    }).subscribe({
        next: (resultado) => {
          this.resultado = resultado;
          this.carregando = false;

          this.cdr.detectChanges();
        },
      error: (err) => {
        this.erro = 'Erro ao calcular resultado. Verifique se o backend está rodando.';
        this.carregando = false;
        console.error(err);

        this.cdr.detectChanges();
      }
    });
  }

  irParaFinal(): void {
    this.router.navigate(['/final']);
  }
}
