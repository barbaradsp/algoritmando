package com.algoritmando.dto;

public record ResultadoResponse(
        Long id,
        String nomeUsuario,
        Long quizId,
        String quizTitulo,
        int pontuacao,
        int acertos,
        int totalPerguntas
) {
}