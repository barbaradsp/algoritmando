import { Injectable } from '@angular/core';

import { RespostaDTO } from '../models/resultado.model';
import { Quiz } from '../models/quiz.model';

@Injectable({
  providedIn: 'root'
})
export class QuizStateService {

  nomeUsuario: string = '';

  quizId: number | null = null;
  quizTitulo: string = '';

  respostas: RespostaDTO[] = [];

  selecionarQuiz(quiz: Quiz): void {

    this.quizId = quiz.id;
    this.quizTitulo = quiz.titulo;

    this.nomeUsuario = '';
    this.respostas = [];
  }

  iniciarQuiz(nomeUsuario: string): void {

    this.nomeUsuario = nomeUsuario;
    this.respostas = [];
  }

  adicionarResposta(
    resposta: RespostaDTO
  ): void {

    this.respostas.push(resposta);
  }

  limparRespostas(): void {
    this.respostas = [];
  }

  reset(): void {

    this.nomeUsuario = '';

    this.quizId = null;
    this.quizTitulo = '';

    this.respostas = [];
  }
}
