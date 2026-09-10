package com.algoritmando.dto;

public record RevisaoResponse(
        Long perguntaId,
        String enunciado,
        String respostaEscolhida,
        String textoRespostaEscolhida,
        String respostaCorreta,
        String textoRespostaCorreta,
        boolean correta
) {
}