export interface RespostaDTO {
  perguntaId: number;
  respostaEscolhida: string;
}

export interface ResultadoRequest {
  nomeUsuario: string;
  quizId: number;
  respostas: RespostaDTO[];
}

export interface RevisaoResposta {
  perguntaId: number;

  enunciado: string;

  respostaEscolhida: string;
  textoRespostaEscolhida: string;

  respostaCorreta: string;
  textoRespostaCorreta: string;

  correta: boolean;
}

export interface ResultadoResponse {
  id: number;

  nomeUsuario: string;

  quizId: number;
  quizTitulo: string;

  pontuacao: number;

  acertos: number;

  totalPerguntas: number;

  revisao: RevisaoResposta[];
}
