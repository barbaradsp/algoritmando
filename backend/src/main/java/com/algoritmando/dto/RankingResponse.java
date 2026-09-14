package com.algoritmando.dto;

import java.time.LocalDateTime;

public record RankingResponse(

        int posicao,

        Long usuarioId,

        String nomeUsuario,

        int pontuacao,

        int acertos,

        int totalPerguntas,

        LocalDateTime dataRealizacao

) {
}