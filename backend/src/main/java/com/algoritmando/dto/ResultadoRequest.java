package com.algoritmando.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ResultadoRequest(

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "O quiz é obrigatório")
        Long quizId,

        @NotEmpty(message = "As respostas são obrigatórias")
        List<@Valid RespostaDTO> respostas

) {
}