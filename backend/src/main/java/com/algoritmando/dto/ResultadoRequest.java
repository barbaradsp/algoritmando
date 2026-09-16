package com.algoritmando.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ResultadoRequest(

        @NotNull(
                message = "O usuário é obrigatório"
        )
        @Positive(
                message = "O identificador do usuário deve ser válido"
        )
        Long usuarioId,

        @NotNull(
                message = "O quiz é obrigatório"
        )
        @Positive(
                message = "O identificador do quiz deve ser válido"
        )
        Long quizId,

        @NotEmpty(
                message = "As respostas são obrigatórias"
        )
        List<
                @Valid RespostaDTO
                > respostas

) {
}