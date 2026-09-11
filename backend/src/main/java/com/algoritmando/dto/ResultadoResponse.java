package com.algoritmando.dto;

import java.util.List;

public record ResultadoResponse(
        Long id,
        Long usuarioId,
        String nomeUsuario,
        String emailUsuario,
        Long quizId,
        String quizTitulo,
        int pontuacao,
        int acertos,
        int totalPerguntas,
        List<RevisaoResponse> revisao
) {
}