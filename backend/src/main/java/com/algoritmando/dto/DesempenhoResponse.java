package com.algoritmando.dto;

public record DesempenhoResponse(

        int totalTentativas,

        long quizzesRealizados,

        int mediaPontuacao,

        int melhorPontuacao

) {
}