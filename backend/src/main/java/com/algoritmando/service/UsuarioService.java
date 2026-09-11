package com.algoritmando.service;

import com.algoritmando.dto.UsuarioRequest;
import com.algoritmando.dto.UsuarioResponse;
import com.algoritmando.model.Usuario;
import com.algoritmando.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponse cadastrarOuBuscar(
            UsuarioRequest request
    ) {

        if (
                request.nome() == null ||
                        request.nome().isBlank()
        ) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Nome não informado"
            );
        }

        if (
                request.email() == null ||
                        request.email().isBlank()
        ) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "E-mail não informado"
            );
        }

        String nome =
                request.nome().trim();

        String email =
                request.email()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        Usuario usuario =
                usuarioRepository
                        .findByEmailIgnoreCase(email)
                        .map(usuarioExistente -> {

                            /*
                             * Caso o mesmo e-mail seja usado novamente,
                             * consideramos que é o mesmo usuário.
                             *
                             * Se o nome tiver sido alterado,
                             * atualizamos o cadastro.
                             */
                            if (
                                    !usuarioExistente
                                            .getNome()
                                            .equals(nome)
                            ) {
                                usuarioExistente
                                        .setNome(nome);

                                return usuarioRepository
                                        .save(usuarioExistente);
                            }

                            return usuarioExistente;
                        })
                        .orElseGet(() -> {

                            Usuario novoUsuario =
                                    Usuario.builder()
                                            .nome(nome)
                                            .email(email)
                                            .build();

                            return usuarioRepository
                                    .save(novoUsuario);
                        });

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}