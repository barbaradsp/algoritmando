import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Pergunta } from '../models/pergunta.model';
import { Quiz } from '../models/quiz.model';
import {
  ResultadoRequest,
  ResultadoResponse
} from '../models/resultado.model';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  cadastrarUsuario(
    nome: string,
    email: string
  ): Observable<any> {

    return this.http.post(
      `${this.baseUrl}/usuarios`,
      {
        nome,
        email
      }
    );
  }

  listarQuizzes(): Observable<Quiz[]> {

    return this.http.get<Quiz[]>(
      `${this.baseUrl}/quizzes`
    );
  }

  listarPerguntasPorQuiz(
    quizId: number
  ): Observable<Pergunta[]> {

    return this.http.get<Pergunta[]>(
      `${this.baseUrl}/quizzes/${quizId}/perguntas`
    );
  }

  enviarResultado(
    request: ResultadoRequest
  ): Observable<ResultadoResponse> {

    return this.http.post<ResultadoResponse>(
      `${this.baseUrl}/resultados`,
      request
    );
  }
}
