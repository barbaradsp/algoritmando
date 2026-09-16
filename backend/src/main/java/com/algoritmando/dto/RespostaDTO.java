package com.algoritmando.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record RespostaDTO(

        @NotNull(
                message = "A pergunta é obrigatória"
        )
        @Positive(
                message = "O identificador da pergunta deve ser válido"
        )
        Long perguntaId,

        @NotBlank(
                message = "A resposta é obrigatória"
        )
        @Pattern(
                regexp = "(?i)^[ABCD]$",
                message = "A resposta deve ser A, B, C ou D"
        )
        String respostaEscolhida

) {
}