import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Cadastro } from './cadastro/cadastro';
import { Quiz } from './quiz/quiz';
import { Pontuacao } from './pontuacao/pontuacao';
import { Final } from './final/final';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'cadastro', component: Cadastro },
  { path: 'quiz', component: Quiz },
  { path: 'pontuacao', component: Pontuacao },
  { path: 'final', component: Final }
];
