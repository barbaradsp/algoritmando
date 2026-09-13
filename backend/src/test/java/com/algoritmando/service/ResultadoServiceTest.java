package com.algoritmando.service;

import com.algoritmando.dto.RespostaDTO;
import com.algoritmando.dto.ResultadoRequest;
import com.algoritmando.dto.ResultadoResponse;

import com.algoritmando.model.Categoria;
import com.algoritmando.model.Dificuldade;
import com.algoritmando.model.Pergunta;
import com.algoritmando.model.Quiz;
import com.algoritmando.model.Resultado;
import com.algoritmando.model.Usuario;

import com.algoritmando.repository.PerguntaRepository;
import com.algoritmando.repository.QuizRepository;
import com.algoritmando.repository.ResultadoRepository;
import com.algoritmando.repository.UsuarioRepository;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceTest {

    @Mock
    private PerguntaRepository perguntaRepository;

    @Mock
    private ResultadoRepository resultadoRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ResultadoService resultadoService;

    private Usuario usuario;

    private Quiz quiz;

    private Pergunta pergunta1;

    private Pergunta pergunta2;

    @BeforeEach
    void setUp() {

        usuario = Usuario.builder()
                .id(1L)
                .nome("Barbara")
                .email("barbara@email.com")
                .build();


        quiz = Quiz.builder()
                .id(1L)
                .titulo("Algoritmos")
                .descricao("Quiz sobre algoritmos")
                .categoria(Categoria.ALGORITMOS)
                .dificuldade(Dificuldade.MEDIO)
                .ativo(true)
                .build();

        pergunta1 = Pergunta.builder()
                .id(1L)
                .enunciado("Qual a complexidade da busca binária?")
                .alternativaA("O(n)")
                .alternativaB("O(log n)")
                .alternativaC("O(n²)")
                .alternativaD("O(1)")
                .respostaCorreta("B")
                .quiz(quiz)
                .build();

        pergunta2 = Pergunta.builder()
                .id(2L)
                .enunciado("Qual algoritmo usa comparações adjacentes?")
                .alternativaA("Bubble Sort")
                .alternativaB("Busca Binária")
                .alternativaC("DFS")
                .alternativaD("BFS")
                .respostaCorreta("A")
                .quiz(quiz)
                .build();
    }

    @Test
    void deveCalcularPontuacaoCorretamente() {

        ResultadoRequest request =
                new ResultadoRequest(
                        1L,
                        1L,
                        List.of(
                                new RespostaDTO(1L, "B"),
                                new RespostaDTO(2L, "D")
                        )
                );

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(quizRepository.findById(1L))
                .thenReturn(Optional.of(quiz));

        when(perguntaRepository.findByQuizIdOrderByIdAsc(1L))
                .thenReturn(List.of(pergunta1, pergunta2));

        when(resultadoRepository.save(any(Resultado.class)))
                .thenAnswer(invocation -> {
                    Resultado resultado = invocation.getArgument(0);
                    resultado.setId(10L);
                    return resultado;
                });

        ResultadoResponse response = resultadoService.calcularResultado(request);

        assertEquals(1, response.acertos());

        assertEquals(2, response.totalPerguntas());

        assertEquals(50, response.pontuacao());

        assertEquals(1L, response.usuarioId());

        assertEquals("Barbara", response.nomeUsuario());

        assertEquals("Algoritmos", response.quizTitulo());

        assertEquals(2, response.revisao().size());

        assertTrue(response.revisao().get(0).correta());

        assertFalse(response.revisao().get(1).correta());
    }

    @Test
    void deveRejeitarPerguntaQueNaoPertenceAoQuiz() {

        ResultadoRequest request =
                new ResultadoRequest(
                        1L,
                        1L,
                        List.of(
                                new RespostaDTO(1L, "B"),
                                new RespostaDTO(999L, "A")
                        )
                );

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(quizRepository.findById(1L))
                .thenReturn(Optional.of(quiz));

        when(perguntaRepository.findByQuizIdOrderByIdAsc(1L))
                .thenReturn(List.of(pergunta1, pergunta2));

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> resultadoService.calcularResultado(request)
                );

        assertEquals(400, exception.getStatusCode().value());

        verify(resultadoRepository, never()).save(any());
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoExiste() {

        ResultadoRequest request =
                new ResultadoRequest(
                        999L,
                        1L,
                        List.of(new RespostaDTO(1L, "B"))
                );

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> resultadoService.calcularResultado(request)
                );

        assertEquals(404, exception.getStatusCode().value());
    }
}