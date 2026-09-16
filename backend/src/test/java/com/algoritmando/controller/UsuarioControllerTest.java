package com.algoritmando.controller;

import com.algoritmando.dto.UsuarioResponse;
import com.algoritmando.exception.GlobalExceptionHandler;
import com.algoritmando.service.UsuarioService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        UsuarioController.class
)
@Import(
        GlobalExceptionHandler.class
)
class UsuarioControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private UsuarioService usuarioService;


    @Test
    void deveCadastrarUsuarioComDadosValidos()
            throws Exception {

        UsuarioResponse response =
                new UsuarioResponse(
                        1L,
                        "Barbara",
                        "barbara@email.com"
                );


        when(
                usuarioService
                        .cadastrarOuBuscar(
                                any()
                        )
        ).thenReturn(
                response
        );


        mockMvc.perform(

                        post(
                                "/api/usuarios"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "nome": "Barbara",
                                          "email": "barbara@email.com"
                                        }
                                        """
                                )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )

                .andExpect(
                        jsonPath("$.nome")
                                .value(
                                        "Barbara"
                                )
                )

                .andExpect(
                        jsonPath("$.email")
                                .value(
                                        "barbara@email.com"
                                )
                );
    }


    @Test
    void deveRejeitarNomeVazio()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/usuarios"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "nome": "",
                                          "email": "barbara@email.com"
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )

                .andExpect(
                        jsonPath("$.mensagem")
                                .value(
                                        "Dados inválidos"
                                )
                )

                .andExpect(
                        jsonPath("$.campos.nome")
                                .value(
                                        "O nome é obrigatório"
                                )
                );


        verify(
                usuarioService,
                never()
        ).cadastrarOuBuscar(
                any()
        );
    }


    @Test
    void deveRejeitarNomeComApenasUmCaractere()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/usuarios"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "nome": "A",
                                          "email": "barbara@email.com"
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
                                "$.campos.nome"
                        )
                                .value(
                                        "O nome deve ter entre 2 e 100 caracteres"
                                )
                );


        verify(
                usuarioService,
                never()
        ).cadastrarOuBuscar(
                any()
        );
    }


    @Test
    void deveRejeitarEmailVazio()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/usuarios"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "nome": "Barbara",
                                          "email": ""
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
                                "$.campos.email"
                        )
                                .value(
                                        "O e-mail é obrigatório"
                                )
                );


        verify(
                usuarioService,
                never()
        ).cadastrarOuBuscar(
                any()
        );
    }


    @Test
    void deveRejeitarEmailInvalido()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/usuarios"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "nome": "Barbara",
                                          "email": "banana"
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
                                "$.campos.email"
                        )
                                .value(
                                        "Informe um e-mail válido"
                                )
                );


        verify(
                usuarioService,
                never()
        ).cadastrarOuBuscar(
                any()
        );
    }


    @Test
    void deveRejeitarJsonInvalido()
            throws Exception {

        mockMvc.perform(

                        post(
                                "/api/usuarios"
                        )

                                .contentType(
                                        MediaType
                                                .APPLICATION_JSON
                                )

                                .content(
                                        """
                                        {
                                          "nome": "Barbara",
                                          "email":
                                        }
                                        """
                                )
                )

                .andExpect(
                        status()
                                .isBadRequest()
                )

                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )

                .andExpect(
                        jsonPath("$.mensagem")
                                .value(
                                        "O corpo da requisição está inválido"
                                )
                );


        verify(
                usuarioService,
                never()
        ).cadastrarOuBuscar(
                any()
        );
    }
}