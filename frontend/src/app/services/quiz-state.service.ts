import { Injectable } from '@angular/core';
import { RespostaDTO } from '../models/resultado.model';

@Injectable({
  providedIn: 'root'
})
export class QuizStateService {
  nomeUsuario: string = '';
  respostas: RespostaDTO[] = [];

  reset(): void {
    this.nomeUsuario = '';
    this.respostas = [];
  }

  adicionarResposta(resposta: RespostaDTO): void {
    this.respostas.push(resposta);
  }
}
