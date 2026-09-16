package com.algoritmando.controller;

import com.algoritmando.dto.DesempenhoResponse;
import com.algoritmando.dto.ResultadoResponse;
import com.algoritmando.exception.GlobalExceptionHandler;
import com.algoritmando.service.ResultadoService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        ResultadoController.class
)
@Import(
        GlobalExceptionHandler.class
)
class ResultadoControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private ResultadoService resultadoService;


    @Test
    void deveRegistrarResultadoValido()
            throws Exception {

        ResultadoResponse response =
                new ResultadoResponse(

                        10L,

                        1L,

                        "Barbara",

                        "barbara@email.com",

                        2L,

                        "Algoritmos",

                        80,

                        4,

                        5,

                        List.of()
                );


        when(
                resultadoService
                        .calcularResultado(
                                any()
                        )
        ).thenReturn(
                response
        );


        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": 1,
                                          "quizId": 2,
                                          "respostas": [
                                            {
                                              "perguntaId": 1,
                                              "respostaEscolhida": "A"
                                            }
                                          ]
                                        }
                                        """
                                )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.id")
                                .value(10)
                )

                .andExpect(
                        jsonPath("$.usuarioId")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.nomeUsuario")
                                .value(
                                        "Barbara"
                                )
                )

                .andExpect(
                        jsonPath("$.pontuacao")
                                .value(80)
                );
    }


    @Test
    void deveRejeitarUsuarioIdNegativo()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": -1,
                                          "quizId": 1,
                                          "respostas": [
                                            {
                                              "perguntaId": 1,
                                              "respostaEscolhida": "A"
                                            }
                                          ]
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath(
                                "$.campos.usuarioId"
                        )
                                .value(
                                        "O identificador do usuário deve ser válido"
                                )
                );


        verify(
                resultadoService,
                never()
        ).calcularResultado(
                any()
        );
    }


    @Test
    void deveRejeitarQuizIdNegativo()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": 1,
                                          "quizId": -1,
                                          "respostas": [
                                            {
                                              "perguntaId": 1,
                                              "respostaEscolhida": "A"
                                            }
                                          ]
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath(
                                "$.campos.quizId"
                        )
                                .value(
                                        "O identificador do quiz deve ser válido"
                                )
                );


        verify(
                resultadoService,
                never()
        ).calcularResultado(
                any()
        );
    }


    @Test
    void deveRejeitarPerguntaIdNegativo()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": 1,
                                          "quizId": 1,
                                          "respostas": [
                                            {
                                              "perguntaId": -10,
                                              "respostaEscolhida": "A"
                                            }
                                          ]
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath(
                                "$.campos['respostas[0].perguntaId']"
                        )
                                .value(
                                        "O identificador da pergunta deve ser válido"
                                )
                );


        verify(
                resultadoService,
                never()
        ).calcularResultado(
                any()
        );
    }


    @Test
    void deveRejeitarAlternativaInvalida()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": 1,
                                          "quizId": 1,
                                          "respostas": [
                                            {
                                              "perguntaId": 1,
                                              "respostaEscolhida": "X"
                                            }
                                          ]
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath(
                                "$.campos['respostas[0].respostaEscolhida']"
                        )
                                .value(
                                        "A resposta deve ser A, B, C ou D"
                                )
                );


        verify(
                resultadoService,
                never()
        ).calcularResultado(
                any()
        );
    }


    @Test
    void deveRejeitarListaDeRespostasVazia()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": 1,
                                          "quizId": 1,
                                          "respostas": []
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath(
                                "$.campos.respostas"
                        )
                                .value(
                                        "As respostas são obrigatórias"
                                )
                );


        verify(
                resultadoService,
                never()
        ).calcularResultado(
                any()
        );
    }


    @Test
    void deveRejeitarEmailInvalidoNoHistorico()
            throws Exception {

        mockMvc.perform(

                        get(
                                "/api/resultados/historico"
                        )

                                .param(
                                        "email",
                                        "banana"
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                );


        verify(
                resultadoService,
                never()
        ).listarHistorico(
                any()
        );
    }


    @Test
    void deveRejeitarEmailInvalidoNoDesempenho()
            throws Exception {

        mockMvc.perform(

                        get(
                                "/api/resultados/desempenho"
                        )

                                .param(
                                        "email",
                                        "banana"
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                );


        verify(
                resultadoService,
                never()
        ).obterDesempenho(
                any()
        );
    }


    @Test
    void deveRetornarDesempenhoParaEmailValido()
            throws Exception {

        DesempenhoResponse response =
                new DesempenhoResponse(
                        5,
                        3,
                        76,
                        100
                );


        when(
                resultadoService
                        .obterDesempenho(
                                "barbara@email.com"
                        )
        ).thenReturn(
                response
        );


        mockMvc.perform(

                        get(
                                "/api/resultados/desempenho"
                        )

                                .param(
                                        "email",
                                        "barbara@email.com"
                                )
                )

                .andExpect(
                        status()
                                .isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.totalTentativas"
                        )
                                .value(5)
                )

                .andExpect(
                        jsonPath(
                                "$.quizzesRealizados"
                        )
                                .value(3)
                )

                .andExpect(
                        jsonPath(
                                "$.mediaPontuacao"
                        )
                                .value(76)
                )

                .andExpect(
                        jsonPath(
                                "$.melhorPontuacao"
                        )
                                .value(100)
                );
    }


    @Test
    void deveRejeitarJsonInvalido()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/resultados"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "usuarioId": 1,
                                          "quizId":
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath("$.mensagem")
                                .value(
                                        "O corpo da requisição está inválido"
                                )
                );


        verify(
                resultadoService,
                never()
        ).calcularResultado(
                any()
        );
    }
}