package com.algoritmando.dto;

import com.algoritmando.model.Categoria;
import com.algoritmando.model.Dificuldade;

import java.time.LocalDateTime;

public record HistoricoResultadoResponse(
        Long id,
        Long quizId,
        String quizTitulo,
        Categoria categoria,
        Dificuldade dificuldade,
        int pontuacao,
        int acertos,
        int totalPerguntas,
        LocalDateTime dataRealizacao
) {
}