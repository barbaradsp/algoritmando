package com.algoritmando.controller;

import com.algoritmando.dto.HistoricoResultadoResponse;
import com.algoritmando.dto.ResultadoRequest;
import com.algoritmando.dto.ResultadoResponse;
import com.algoritmando.service.ResultadoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
                        .calcularResultado(
                                request
                        )
        );
    }

    @GetMapping("/historico")
    public ResponseEntity<
            List<HistoricoResultadoResponse>
            > listarHistorico(

            @RequestParam
            String email

    ) {

        return ResponseEntity.ok(
                resultadoService
                        .listarHistorico(
                                email
                        )
        );
    }
}