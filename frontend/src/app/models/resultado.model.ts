export interface RespostaDTO {
  perguntaId: number;
  respostaEscolhida: string;
}

export interface ResultadoRequest {
  nomeUsuario: string;
  quizId: number;
  respostas: RespostaDTO[];
}

export interface ResultadoResponse {
  id: number;
  nomeUsuario: string;

  quizId: number;
  quizTitulo: string;

  pontuacao: number;
  acertos: number;
  totalPerguntas: number;
}
