import {
  ChangeDetectorRef,
  Component
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { ApiService } from '../services/api.service';
import { QuizStateService } from '../services/quiz-state.service';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css'
})
export class Cadastro {

  nome: string = '';
  email: string = '';

  carregando: boolean = false;
  erro: string = '';

  constructor(
    private api: ApiService,
    public quizState: QuizStateService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  cadastrar(): void {

    if (
      !this.quizState.quizId
    ) {
      this.router.navigate(['/']);
      return;
    }

    if (
      !this.nome ||
      !this.email
    ) {
      return;
    }

    this.carregando = true;
    this.erro = '';

    this.api
      .cadastrarUsuario(
        this.nome,
        this.email
      )
      .subscribe({

        next: () => {

          this.quizState
            .iniciarQuiz(
              this.nome
            );

          this.carregando = false;

          this.router.navigate([
            '/quiz'
          ]);
        },

        error: (err) => {

          console.error(
            'Erro ao cadastrar usuário:',
            err
          );

          this.erro =
            'Erro ao cadastrar. Verifique se o backend está rodando.';

          this.carregando = false;

          this.cdr.detectChanges();
        }

      });
  }
}
