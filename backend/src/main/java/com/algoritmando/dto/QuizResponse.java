package com.algoritmando.dto;

import com.algoritmando.model.Categoria;
import com.algoritmando.model.Dificuldade;

public record QuizResponse(
        Long id,
        String titulo,
        String descricao,
        Categoria categoria,
        Dificuldade dificuldade,
        long quantidadePerguntas
) {
}