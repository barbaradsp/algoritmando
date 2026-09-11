package com.algoritmando.controller;

import com.algoritmando.dto.UsuarioRequest;
import com.algoritmando.dto.UsuarioResponse;
import com.algoritmando.service.UsuarioService;
import jakarta.validation.Valid;
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

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
            @Valid
            @RequestBody
            UsuarioRequest request
    ) {
        return ResponseEntity.ok(
                usuarioService.cadastrarOuBuscar(request)
        );
    }
}