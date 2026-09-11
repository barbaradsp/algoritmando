package com.algoritmando.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RespostaDTO(

        @NotNull(message = "A pergunta é obrigatória")
        Long perguntaId,

        @NotBlank(message = "A resposta é obrigatória")
        @Pattern(
                regexp = "^[ABCDabcd]$",
                message = "A resposta deve ser A, B, C ou D"
        )
        String respostaEscolhida

) {
}