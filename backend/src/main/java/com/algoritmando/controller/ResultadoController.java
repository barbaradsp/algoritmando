package com.algoritmando.controller;

import com.algoritmando.dto.ResultadoRequest;
import com.algoritmando.dto.ResultadoResponse;
import com.algoritmando.service.ResultadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resultados")
@RequiredArgsConstructor
public class ResultadoController {

    private final ResultadoService resultadoService;

    @PostMapping
    public ResponseEntity<ResultadoResponse> registrar(
            @RequestBody ResultadoRequest request
    ) {
        return ResponseEntity.ok(
                resultadoService
                        .calcularResultado(request)
        );
    }
}