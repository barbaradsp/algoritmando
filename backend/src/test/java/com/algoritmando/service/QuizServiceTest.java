package com.algoritmando.service;

import com.algoritmando.dto.PerguntaResponse;
import com.algoritmando.model.Categoria;
import com.algoritmando.model.Dificuldade;
import com.algoritmando.model.Pergunta;
import com.algoritmando.model.Quiz;
import com.algoritmando.repository.PerguntaRepository;
import com.algoritmando.repository.QuizRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private PerguntaRepository perguntaRepository;

    @InjectMocks
    private QuizService quizService;

    private Quiz quiz;

    @BeforeEach
    void setUp() {

        quiz = Quiz.builder()
                .id(1L)
                .titulo("Estruturas de Dados")
                .descricao(
                        "Quiz sobre estruturas de dados"
                )
                .categoria(
                        Categoria.ESTRUTURAS_DE_DADOS
                )
                .dificuldade(
                        Dificuldade.FACIL
                )
                .ativo(true)
                .build();
    }

    @Test
    void deveListarPerguntasDoQuiz() {

        Pergunta pergunta =
                Pergunta.builder()
                        .id(1L)
                        .enunciado(
                                "Qual estrutura segue LIFO?"
                        )
                        .alternativaA("Fila")
                        .alternativaB("Lista")
                        .alternativaC("Pilha")
                        .alternativaD("Árvore")
                        .respostaCorreta("C")
                        .quiz(quiz)
                        .build();

        when(
                quizRepository.findById(1L)
        ).thenReturn(
                Optional.of(quiz)
        );

        when(
                perguntaRepository
                        .findByQuizIdOrderByIdAsc(1L)
        ).thenReturn(
                List.of(pergunta)
        );

        List<PerguntaResponse> respostas =
                quizService
                        .listarPerguntas(1L);

        assertEquals(
                1,
                respostas.size()
        );

        assertEquals(
                "Qual estrutura segue LIFO?",
                respostas.get(0).enunciado()
        );

        assertEquals(
                "Pilha",
                respostas.get(0).alternativaC()
        );
    }

    @Test
    void deveRetornarErroQuandoQuizNaoExiste() {

        when(
                quizRepository.findById(999L)
        ).thenReturn(
                Optional.empty()
        );

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () ->
                                quizService
                                        .listarPerguntas(
                                                999L
                                        )
                );

        assertEquals(
                404,
                exception
                        .getStatusCode()
                        .value()
        );
    }
}