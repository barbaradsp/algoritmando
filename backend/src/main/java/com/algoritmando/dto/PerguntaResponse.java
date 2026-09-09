package com.algoritmando.dto;

public record PerguntaResponse(
        Long id,
        String enunciado,
        String alternativaA,
        String alternativaB,
        String alternativaC,
        String alternativaD
) {
}