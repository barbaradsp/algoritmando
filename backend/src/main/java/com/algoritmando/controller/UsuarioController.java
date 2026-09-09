package com.algoritmando.controller;

import com.algoritmando.dto.UsuarioRequest;
import com.algoritmando.dto.UsuarioResponse;
import com.algoritmando.model.Usuario;
import com.algoritmando.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody UsuarioRequest request) {
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .build();

        usuario = usuarioRepository.save(usuario);

        return ResponseEntity.ok(new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail()));
    }
}