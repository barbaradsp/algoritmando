import {
  Injectable
} from '@angular/core';

import {
  RespostaDTO,
  ResultadoResponse
} from '../models/resultado.model';

import {
  Quiz
} from '../models/quiz.model';

import {
  UsuarioResponse
} from '../models/usuario.model';


@Injectable({
  providedIn: 'root'
})
export class QuizStateService {

  usuarioId:
    number | null = null;

  nomeUsuario:
    string = '';

  emailUsuario:
    string = '';


  quizId:
    number | null = null;

  quizTitulo:
    string = '';


  respostas:
    RespostaDTO[] = [];


  resultadoAtual:
    ResultadoResponse | null = null;


  selecionarQuiz(
    quiz: Quiz
  ): void {

    this.quizId =
      quiz.id;

    this.quizTitulo =
      quiz.titulo;

    this.usuarioId =
      null;

    this.nomeUsuario =
      '';

    this.emailUsuario =
      '';

    this.respostas =
      [];

    this.resultadoAtual =
      null;
  }


  iniciarQuiz(
    usuario: UsuarioResponse
  ): void {

    this.usuarioId =
      usuario.id;

    this.nomeUsuario =
      usuario.nome;

    this.emailUsuario =
      usuario.email;

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

    this.usuarioId =
      null;

    this.nomeUsuario =
      '';

    this.emailUsuario =
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
