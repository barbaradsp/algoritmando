export interface RespostaDTO {
  perguntaId: number;
  respostaEscolhida: string;
}

export interface ResultadoRequest {
  nomeUsuario: string;
  respostas: RespostaDTO[];
}

export interface ResultadoResponse {
  id: number;
  nomeUsuario: string;
  pontuacao: number;
  acertos: number;
  totalPerguntas: number;
}
