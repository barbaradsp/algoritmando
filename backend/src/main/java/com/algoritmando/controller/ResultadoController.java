package com.algoritmando.controller;

import com.algoritmando.dto.DesempenhoResponse;
import com.algoritmando.dto.HistoricoResultadoResponse;
import com.algoritmando.dto.ResultadoRequest;
import com.algoritmando.dto.ResultadoResponse;
import com.algoritmando.service.ResultadoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/resultados")
@RequiredArgsConstructor
public class ResultadoController {

    private final ResultadoService resultadoService;

    @PostMapping
    public ResponseEntity<ResultadoResponse> registrar(
            @Valid
            @RequestBody
            ResultadoRequest request
    ) {

        return ResponseEntity.ok(
                resultadoService.calcularResultado(request)
        );
    }

    @GetMapping("/historico")
    public ResponseEntity<List<HistoricoResultadoResponse>> listarHistorico(

            @RequestParam
            @NotBlank(
                    message = "O e-mail é obrigatório"
            )
            @Email(
                    message = "Informe um e-mail válido"
            )
            String email
    ) {

        return ResponseEntity.ok(
                resultadoService
                        .listarHistorico(
                                email
                        )
        );
    }

    @GetMapping("/desempenho")
    public ResponseEntity<DesempenhoResponse>
    obterDesempenho(

            @RequestParam
            @NotBlank(
                    message = "O e-mail é obrigatório"
            )
            @Email(
                    message = "Informe um e-mail válido"
            )
            String email
    ) {

        return ResponseEntity.ok(
                resultadoService
                        .obterDesempenho(
                                email
                        )
        );
    }
}