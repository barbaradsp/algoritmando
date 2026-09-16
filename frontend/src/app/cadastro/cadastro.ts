import {
  ChangeDetectorRef,
  Component
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';

import {
  HttpErrorResponse
} from '@angular/common/http';

import {
  Router
} from '@angular/router';

import {
  ApiService
} from '../services/api.service';

import {
  QuizStateService
} from '../services/quiz-state.service';

import {
  ApiError
} from '../models/api-error.model';


function nomeValido(): ValidatorFn {

  return (
    control: AbstractControl
  ): ValidationErrors | null => {

    const valor =
      String(
        control.value ?? ''
      );

    if (
      valor.length > 0 &&
      valor.trim().length === 0
    ) {

      return {
        somenteEspacos: true
      };
    }

    if (
      valor.trim().length > 0 &&
      valor.trim().length < 2
    ) {

      return {
        nomeCurto: true
      };
    }

    return null;
  };
}


@Component({
  selector: 'app-cadastro',

  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule
  ],

  templateUrl:
    './cadastro.html',

  styleUrl:
    './cadastro.css'
})
export class Cadastro {

  carregando = false;

  erroGeral = '';

  errosServidor:
    Record<string, string> = {};


  cadastroForm =
    new FormGroup({

      nome:
        new FormControl(
          '',
          {
            nonNullable: true,

            validators: [
              Validators.required,
              Validators.maxLength(100),
              nomeValido()
            ]
          }
        ),

      email:
        new FormControl(
          '',
          {
            nonNullable: true,

            validators: [
              Validators.required,
              Validators.email,
              Validators.maxLength(254)
            ]
          }
        )
    });


  constructor(

    private api:
    ApiService,

    public quizState:
    QuizStateService,

    private router:
    Router,

    private cdr:
    ChangeDetectorRef

  ) {}


  cadastrar(): void {

    this.erroGeral = '';

    this.errosServidor = {};


    if (
      !this.quizState.quizId
    ) {

      this.router.navigate([
        '/'
      ]);

      return;
    }


    if (
      this.cadastroForm.invalid
    ) {

      this.cadastroForm
        .markAllAsTouched();

      return;
    }


    const nome =
      this.cadastroForm.controls
        .nome.value
        .trim();

    const email =
      this.cadastroForm.controls
        .email.value
        .trim()
        .toLowerCase();


    if (nome.length < 2) {

      this.cadastroForm.controls
        .nome
        .setErrors({
          nomeCurto: true
        });

      this.cadastroForm.controls
        .nome
        .markAsTouched();

      return;
    }


    this.carregando = true;


    this.api
      .cadastrarUsuario(
        nome,
        email
      )
      .subscribe({

        next: (
          usuario
        ) => {

          this.quizState
            .iniciarQuiz(
              usuario
            );

          this.carregando = false;

          this.router.navigate([
            '/quiz'
          ]);
        },


        error: (
          erro: HttpErrorResponse
        ) => {

          console.error(
            'Erro ao cadastrar usuário:',
            erro
          );

          this.tratarErro(
            erro
          );

          this.carregando = false;

          this.cdr
            .detectChanges();
        }

      });
  }


  campoInvalido(
    campo: 'nome' | 'email'
  ): boolean {

    const controle =
      this.cadastroForm.controls[
        campo
        ];

    return (
      (
        controle.invalid &&
        (
          controle.touched ||
          controle.dirty
        )
      ) ||
      !!this.errosServidor[campo]
    );
  }


  mensagemNome(): string {

    if (
      this.errosServidor['nome']
    ) {

      return this.errosServidor[
        'nome'
        ];
    }


    const controle =
      this.cadastroForm.controls.nome;


    if (
      controle.hasError(
        'required'
      )
    ) {

      return 'O nome é obrigatório.';
    }


    if (
      controle.hasError(
        'somenteEspacos'
      )
    ) {

      return 'O nome não pode conter somente espaços.';
    }


    if (
      controle.hasError(
        'nomeCurto'
      )
    ) {

      return 'O nome deve ter pelo menos 2 caracteres.';
    }


    if (
      controle.hasError(
        'maxlength'
      )
    ) {

      return 'O nome deve ter no máximo 100 caracteres.';
    }


    return '';
  }


  mensagemEmail(): string {

    if (
      this.errosServidor['email']
    ) {

      return this.errosServidor[
        'email'
        ];
    }


    const controle =
      this.cadastroForm.controls.email;


    if (
      controle.hasError(
        'required'
      )
    ) {

      return 'O e-mail é obrigatório.';
    }


    if (
      controle.hasError(
        'email'
      )
    ) {

      return 'Informe um e-mail válido.';
    }


    if (
      controle.hasError(
        'maxlength'
      )
    ) {

      return 'O e-mail deve ter no máximo 254 caracteres.';
    }


    return '';
  }


  limparErroServidor(
    campo: 'nome' | 'email'
  ): void {

    if (
      this.errosServidor[campo]
    ) {

      delete this.errosServidor[
        campo
        ];
    }


    this.erroGeral = '';
  }


  private tratarErro(
    erro: HttpErrorResponse
  ): void {


    if (erro.status === 0) {

      this.erroGeral =
        'Não foi possível conectar ao servidor. Verifique se o backend está em execução.';

      return;
    }


    const resposta =
      erro.error as ApiError;


    if (
      resposta?.campos &&
      Object.keys(
        resposta.campos
      ).length > 0
    ) {

      this.errosServidor = {
        ...resposta.campos
      };

      return;
    }


    if (
      resposta?.mensagem
    ) {

      this.erroGeral =
        resposta.mensagem;

      return;
    }


    if (
      erro.status >= 500
    ) {

      this.erroGeral =
        'Ocorreu um erro no servidor. Tente novamente.';

      return;
    }


    this.erroGeral =
      'Não foi possível realizar o cadastro.';
  }
}
