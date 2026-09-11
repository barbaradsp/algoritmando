export interface RespostaDTO {
  perguntaId: number;
  respostaEscolhida: string;
}


export interface ResultadoRequest {
  usuarioId: number;
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
  usuarioId: number;
  nomeUsuario: string;
  emailUsuario: string;
  quizId: number;
  quizTitulo: string;
  pontuacao: number;
  acertos: number;
  totalPerguntas: number;
  revisao: RevisaoResposta[];
}


export interface HistoricoResultado {
  id: number;
  quizId: number;
  quizTitulo: string;
  categoria: string;
  dificuldade: string;
  pontuacao: number;
  acertos: number;
  totalPerguntas: number;
  dataRealizacao: string;
}
