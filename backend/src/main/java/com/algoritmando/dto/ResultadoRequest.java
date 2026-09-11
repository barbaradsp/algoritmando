package com.algoritmando.dto;

import java.util.List;

public record ResultadoRequest(
        Long usuarioId,
        Long quizId,
        List<RespostaDTO> respostas
) {
}