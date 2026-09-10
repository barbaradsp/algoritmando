package com.algoritmando.dto;

import java.util.List;

public record ResultadoRequest(
        String nomeUsuario,
        Long quizId,
        List<RespostaDTO> respostas
) {
}