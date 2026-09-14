import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Cadastro } from './cadastro/cadastro';
import { Quiz } from './quiz/quiz';
import { Pontuacao } from './pontuacao/pontuacao';
import { Revisao } from './revisao/revisao';
import { Historico } from './historico/historico';
import { Final } from './final/final';
import { Ranking } from './ranking/ranking';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'cadastro', component: Cadastro },
  { path: 'quiz', component: Quiz },
  { path: 'pontuacao', component: Pontuacao },
  { path: 'revisao', component: Revisao },
  { path: 'historico', component: Historico },
  { path: 'ranking', component: Ranking },
  { path: 'final', component: Final }
];


