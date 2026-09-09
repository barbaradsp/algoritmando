package com.algoritmando.dto;

public record ResultadoResponse(Long id, String nomeUsuario, int pontuacao, int acertos, int totalPerguntas) {
}
