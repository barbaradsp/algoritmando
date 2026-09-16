package com.algoritmando.service;

import com.algoritmando.dto.RespostaDTO;
import com.algoritmando.dto.ResultadoRequest;
import com.algoritmando.dto.ResultadoResponse;
import com.algoritmando.dto.DesempenhoResponse;
import com.algoritmando.dto.RankingResponse;

import java.time.LocalDateTime;

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

    @Test
    void deveManterSomenteMelhorTentativaDeCadaUsuarioNoRanking() {

        Usuario outroUsuario =
                Usuario.builder()
                        .id(2L)
                        .nome("Ana")
                        .email("ana@email.com")
                        .build();


        Resultado tentativaBarbara60 =
                Resultado.builder()
                        .id(1L)
                        .usuario(usuario)
                        .quiz(quiz)
                        .pontuacao(60)
                        .acertos(3)
                        .totalPerguntas(5)
                        .dataRealizacao(
                                LocalDateTime.of(
                                        2026,
                                        9,
                                        10,
                                        10,
                                        0
                                )
                        )
                        .build();


        Resultado tentativaBarbara100 =
                Resultado.builder()
                        .id(2L)
                        .usuario(usuario)
                        .quiz(quiz)
                        .pontuacao(100)
                        .acertos(5)
                        .totalPerguntas(5)
                        .dataRealizacao(
                                LocalDateTime.of(
                                        2026,
                                        9,
                                        11,
                                        10,
                                        0
                                )
                        )
                        .build();


        Resultado tentativaAna80 =
                Resultado.builder()
                        .id(3L)
                        .usuario(outroUsuario)
                        .quiz(quiz)
                        .pontuacao(80)
                        .acertos(4)
                        .totalPerguntas(5)
                        .dataRealizacao(
                                LocalDateTime.of(
                                        2026,
                                        9,
                                        12,
                                        10,
                                        0
                                )
                        )
                        .build();


        when(
                quizRepository.findById(1L)
        ).thenReturn(
                Optional.of(quiz)
        );

        when(
                resultadoRepository
                        .findByQuiz_IdOrderByPontuacaoDescAcertosDescDataRealizacaoAsc(
                                1L
                        )
        ).thenReturn(
                List.of(
                        tentativaBarbara100,
                        tentativaAna80,
                        tentativaBarbara60
                )
        );


        List<RankingResponse> ranking =
                resultadoService
                        .listarRanking(1L);


        assertEquals(
                2,
                ranking.size()
        );


        assertEquals(
                "Barbara",
                ranking.get(0).nomeUsuario()
        );

        assertEquals(
                100,
                ranking.get(0).pontuacao()
        );

        assertEquals(
                1,
                ranking.get(0).posicao()
        );


        assertEquals(
                "Ana",
                ranking.get(1).nomeUsuario()
        );

        assertEquals(
                80,
                ranking.get(1).pontuacao()
        );

        assertEquals(
                2,
                ranking.get(1).posicao()
        );


        long quantidadeBarbara =
                ranking
                        .stream()
                        .filter(
                                resultado ->
                                        resultado
                                                .usuarioId()
                                                .equals(
                                                        usuario.getId()
                                                )
                        )
                        .count();


        assertEquals(
                1,
                quantidadeBarbara
        );
    }

    @Test
    void deveRetornarErroAoBuscarRankingDeQuizInexistente() {

        when(
                quizRepository.findById(999L)
        ).thenReturn(
                Optional.empty()
        );


        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,

                        () ->
                                resultadoService
                                        .listarRanking(
                                                999L
                                        )
                );


        assertEquals(
                404,
                exception
                        .getStatusCode()
                        .value()
        );


        verify(
                resultadoRepository,
                never()
        )
                .findByQuiz_IdOrderByPontuacaoDescAcertosDescDataRealizacaoAsc(
                        any()
                );
    }

    @Test
    void deveCalcularDesempenhoDoUsuarioCorretamente() {

        Quiz outroQuiz =
                Quiz.builder()
                        .id(2L)
                        .titulo(
                                "Complexidade de Algoritmos"
                        )
                        .descricao(
                                "Quiz sobre complexidade"
                        )
                        .categoria(
                                Categoria.COMPLEXIDADE
                        )
                        .dificuldade(
                                Dificuldade.MEDIO
                        )
                        .ativo(true)
                        .build();


        Resultado resultado1 =
                Resultado.builder()
                        .id(1L)
                        .usuario(usuario)
                        .quiz(quiz)
                        .pontuacao(40)
                        .acertos(2)
                        .totalPerguntas(5)
                        .build();


        Resultado resultado2 =
                Resultado.builder()
                        .id(2L)
                        .usuario(usuario)
                        .quiz(quiz)
                        .pontuacao(60)
                        .acertos(3)
                        .totalPerguntas(5)
                        .build();


        Resultado resultado3 =
                Resultado.builder()
                        .id(3L)
                        .usuario(usuario)
                        .quiz(outroQuiz)
                        .pontuacao(100)
                        .acertos(5)
                        .totalPerguntas(5)
                        .build();


        when(
                usuarioRepository
                        .findByEmailIgnoreCase(
                                "barbara@email.com"
                        )
        ).thenReturn(
                Optional.of(usuario)
        );


        when(
                resultadoRepository
                        .findByUsuario_IdOrderByDataRealizacaoDesc(
                                1L
                        )
        ).thenReturn(
                List.of(
                        resultado3,
                        resultado2,
                        resultado1
                )
        );


        DesempenhoResponse desempenho =
                resultadoService
                        .obterDesempenho(
                                "barbara@email.com"
                        );


        assertEquals(
                3,
                desempenho.totalTentativas()
        );

        assertEquals(
                2,
                desempenho.quizzesRealizados()
        );


        assertEquals(
                67,
                desempenho.mediaPontuacao()
        );


        assertEquals(
                100,
                desempenho.melhorPontuacao()
        );
    }

    @Test
    void deveRetornarDesempenhoZeradoQuandoUsuarioNaoPossuiTentativas() {

        when(
                usuarioRepository
                        .findByEmailIgnoreCase(
                                "barbara@email.com"
                        )
        ).thenReturn(
                Optional.of(usuario)
        );


        when(
                resultadoRepository
                        .findByUsuario_IdOrderByDataRealizacaoDesc(
                                1L
                        )
        ).thenReturn(
                List.of()
        );


        DesempenhoResponse desempenho =
                resultadoService
                        .obterDesempenho(
                                "barbara@email.com"
                        );


        assertEquals(
                0,
                desempenho.totalTentativas()
        );

        assertEquals(
                0,
                desempenho.quizzesRealizados()
        );

        assertEquals(
                0,
                desempenho.mediaPontuacao()
        );

        assertEquals(
                0,
                desempenho.melhorPontuacao()
        );
    }

    @Test
    void deveBuscarDesempenhoPeloUsuarioDoEmailInformado() {

        when(
                usuarioRepository
                        .findByEmailIgnoreCase(
                                "barbara@email.com"
                        )
        ).thenReturn(
                Optional.of(usuario)
        );


        when(
                resultadoRepository
                        .findByUsuario_IdOrderByDataRealizacaoDesc(
                                usuario.getId()
                        )
        ).thenReturn(
                List.of()
        );


        resultadoService
                .obterDesempenho(
                        "barbara@email.com"
                );


        verify(
                usuarioRepository
        )
                .findByEmailIgnoreCase(
                        "barbara@email.com"
                );


        verify(
                resultadoRepository
        )
                .findByUsuario_IdOrderByDataRealizacaoDesc(
                        usuario.getId()
                );
    }
}