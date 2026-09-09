import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { QuizStateService } from '../services/quiz-state.service';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
    private quizState: QuizStateService,
    private router: Router
  ) {}

  cadastrar(): void {
    if (!this.nome || !this.email) return;

    this.carregando = true;
    this.erro = '';

    this.api.cadastrarUsuario(this.nome, this.email).subscribe({
      next: () => {
        this.quizState.reset();
        this.quizState.nomeUsuario = this.nome;
        this.carregando = false;
        this.router.navigate(['/quiz']);
      },
      error: (err) => {
        this.carregando = false;
        this.erro = 'Erro ao cadastrar. Verifique se o backend está rodando.';
        console.error(err);
      }
    });
  }
}
