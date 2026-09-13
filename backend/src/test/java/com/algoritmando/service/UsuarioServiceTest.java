package com.algoritmando.service;

import com.algoritmando.dto.UsuarioRequest;
import com.algoritmando.dto.UsuarioResponse;
import com.algoritmando.model.Usuario;
import com.algoritmando.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {

        usuario = Usuario.builder()
                .id(1L)
                .nome("Barbara")
                .email("barbara@email.com")
                .build();
    }

    @Test
    void deveCriarNovoUsuarioQuandoEmailNaoExiste() {

        UsuarioRequest request =
                new UsuarioRequest(
                        "Barbara",
                        "barbara@email.com"
                );

        when(
                usuarioRepository
                        .findByEmailIgnoreCase(
                                "barbara@email.com"
                        )
        ).thenReturn(
                Optional.empty()
        );

        when(
                usuarioRepository.save(any(Usuario.class))
        ).thenReturn(usuario);


        UsuarioResponse response =
                usuarioService
                        .cadastrarOuBuscar(request);

        assertEquals(
                1L,
                response.id()
        );

        assertEquals(
                "Barbara",
                response.nome()
        );

        assertEquals(
                "barbara@email.com",
                response.email()
        );

        verify(
                usuarioRepository,
                times(1)
        ).save(any(Usuario.class));
    }

    @Test
    void deveRetornarMesmoUsuarioQuandoEmailJaExiste() {

        UsuarioRequest request =
                new UsuarioRequest(
                        "Barbara",
                        "barbara@email.com"
                );

        when(
                usuarioRepository
                        .findByEmailIgnoreCase(
                                "barbara@email.com"
                        )
        ).thenReturn(
                Optional.of(usuario)
        );

        UsuarioResponse response =
                usuarioService
                        .cadastrarOuBuscar(request);

        assertEquals(
                usuario.getId(),
                response.id()
        );

        assertEquals(
                usuario.getEmail(),
                response.email()
        );

        verify(
                usuarioRepository,
                never()
        ).save(any(Usuario.class));
    }

    @Test
    void deveNormalizarEmailParaMinusculo() {

        UsuarioRequest request =
                new UsuarioRequest(
                        "Barbara",
                        "BARBARA@EMAIL.COM"
                );

        when(
                usuarioRepository
                        .findByEmailIgnoreCase(
                                "barbara@email.com"
                        )
        ).thenReturn(
                Optional.empty()
        );

        when(
                usuarioRepository.save(any(Usuario.class))
        ).thenAnswer(invocation -> {

            Usuario salvo =
                    invocation.getArgument(0);

            salvo.setId(1L);

            return salvo;
        });

        UsuarioResponse response =
                usuarioService
                        .cadastrarOuBuscar(request);

        assertEquals(
                "barbara@email.com",
                response.email()
        );
    }
}