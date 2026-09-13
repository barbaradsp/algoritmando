package com.algoritmando.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiError> tratarValidacao(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> campos =
                new LinkedHashMap<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(
                        erro ->
                                campos.put(
                                        erro.getField(),
                                        erro.getDefaultMessage()
                                )
                );

        ApiError resposta =
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        "Dados inválidos",
                        campos,
                        LocalDateTime.now()
                );

        return ResponseEntity
                .badRequest()
                .body(resposta);
    }

    @ExceptionHandler(
            ResponseStatusException.class
    )
    public ResponseEntity<ApiError> tratarResponseStatus(
            ResponseStatusException exception
    ) {

        ApiError resposta =
                new ApiError(
                        exception.getStatusCode().value(),
                        exception.getReason(),
                        Map.of(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(
                        exception.getStatusCode()
                )
                .body(resposta);
    }

    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<ApiError> tratarErroInesperado(
            Exception exception
    ) {

        ApiError resposta =
                new ApiError(
                        HttpStatus
                                .INTERNAL_SERVER_ERROR
                                .value(),
                        "Erro interno do servidor",
                        Map.of(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(resposta);
    }
}