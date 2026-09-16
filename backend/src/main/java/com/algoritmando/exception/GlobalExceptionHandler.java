package com.algoritmando.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;

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
                .stream()
                .sorted(
                        (erro1, erro2) ->
                                Integer.compare(
                                        prioridadeValidacao(
                                                erro1.getCode()
                                        ),
                                        prioridadeValidacao(
                                                erro2.getCode()
                                        )
                                )
                )
                .forEach(
                        erro ->
                                campos.putIfAbsent(
                                        erro.getField(),
                                        erro.getDefaultMessage()
                                )
                );


        ApiError resposta =
                new ApiError(

                        HttpStatus
                                .BAD_REQUEST
                                .value(),

                        "Dados inválidos",

                        campos,

                        LocalDateTime.now()
                );


        return ResponseEntity
                .badRequest()
                .body(
                        resposta
                );
    }


    @ExceptionHandler(
            ConstraintViolationException.class
    )
    public ResponseEntity<ApiError>
    tratarConstraintViolation(

            ConstraintViolationException exception

    ) {

        Map<String, String> campos =
                new LinkedHashMap<>();


        exception
                .getConstraintViolations()
                .forEach(
                        violacao -> {

                            String caminho =
                                    violacao
                                            .getPropertyPath()
                                            .toString();


                            String campo =
                                    caminho.contains(".")
                                            ? caminho.substring(
                                            caminho
                                                    .lastIndexOf(".")
                                                    + 1
                                    )
                                            : caminho;


                            campos.put(
                                    campo,
                                    violacao
                                            .getMessage()
                            );
                        }
                );


        ApiError resposta =
                new ApiError(

                        HttpStatus
                                .BAD_REQUEST
                                .value(),

                        "Parâmetros inválidos",

                        campos,

                        LocalDateTime.now()
                );


        return ResponseEntity
                .badRequest()
                .body(
                        resposta
                );
    }


    @ExceptionHandler(
            HttpMessageNotReadableException.class
    )
    public ResponseEntity<ApiError>
    tratarJsonInvalido(

            HttpMessageNotReadableException exception

    ) {

        ApiError resposta =
                new ApiError(

                        HttpStatus
                                .BAD_REQUEST
                                .value(),

                        "O corpo da requisição está inválido",

                        Map.of(),

                        LocalDateTime.now()
                );


        return ResponseEntity
                .badRequest()
                .body(
                        resposta
                );
    }


    @ExceptionHandler(
            ResponseStatusException.class
    )
    public ResponseEntity<ApiError>
    tratarResponseStatus(

            ResponseStatusException exception

    ) {

        ApiError resposta =
                new ApiError(

                        exception
                                .getStatusCode()
                                .value(),

                        exception
                                .getReason(),

                        Map.of(),

                        LocalDateTime.now()
                );


        return ResponseEntity
                .status(
                        exception
                                .getStatusCode()
                )
                .body(
                        resposta
                );
    }


    @ExceptionHandler(
            DataIntegrityViolationException.class
    )
    public ResponseEntity<ApiError>
    tratarIntegridadeDados(

            DataIntegrityViolationException exception

    ) {

        ApiError resposta =
                new ApiError(

                        HttpStatus
                                .CONFLICT
                                .value(),

                        "Existe um conflito com os dados informados",

                        Map.of(),

                        LocalDateTime.now()
                );


        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                )
                .body(
                        resposta
                );
    }


    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<ApiError>
    tratarErroInesperado(

            Exception exception

    ) {

        exception.printStackTrace();


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
                        HttpStatus
                                .INTERNAL_SERVER_ERROR
                )
                .body(
                        resposta
                );
    }

    private int prioridadeValidacao(
            String codigo
    ) {

        if (codigo == null) {
            return 100;
        }


        return switch (codigo) {

            case "NotBlank",
                 "NotNull",
                 "NotEmpty" -> 1;

            case "Email",
                 "Pattern",
                 "Positive" -> 2;

            case "Size" -> 3;

            default -> 10;
        };
    }
}