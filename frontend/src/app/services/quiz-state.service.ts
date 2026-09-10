import { Injectable } from '@angular/core';

import {
  RespostaDTO,
  ResultadoResponse
} from '../models/resultado.model';

import { Quiz } from '../models/quiz.model';

@Injectable({
  providedIn: 'root'
})
export class QuizStateService {

  nomeUsuario: string = '';

  quizId: number | null = null;

  quizTitulo: string = '';

  respostas: RespostaDTO[] = [];

  resultadoAtual:
    ResultadoResponse | null = null;

  selecionarQuiz(
    quiz: Quiz
  ): void {

    this.quizId =
      quiz.id;

    this.quizTitulo =
      quiz.titulo;

    this.nomeUsuario =
      '';

    this.respostas =
      [];

    this.resultadoAtual =
      null;
  }

  iniciarQuiz(
    nomeUsuario: string
  ): void {

    this.nomeUsuario =
      nomeUsuario;

    this.respostas =
      [];

    this.resultadoAtual =
      null;
  }

  adicionarResposta(
    resposta: RespostaDTO
  ): void {

    this.respostas.push(
      resposta
    );
  }

  salvarResultado(
    resultado: ResultadoResponse
  ): void {

    this.resultadoAtual =
      resultado;
  }

  limparRespostas(): void {

    this.respostas =
      [];
  }

  reset(): void {

    this.nomeUsuario =
      '';

    this.quizId =
      null;

    this.quizTitulo =
      '';

    this.respostas =
      [];

    this.resultadoAtual =
      null;
  }
}
