import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { QuizStateService } from '../services/quiz-state.service';

@Component({
  selector: 'app-final',
  standalone: true,
  imports: [],
  templateUrl: './final.html',
  styleUrl: './final.css'
})
export class Final {
  constructor(
    private quizState: QuizStateService,
    private router: Router
  ) {}

  reiniciar(): void {
    this.quizState.reset();
    this.router.navigate(['/']);
  }
}
