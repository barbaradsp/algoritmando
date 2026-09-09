package com.algoritmando.dto;

import java.util.List;

public record ResultadoRequest(String nomeUsuario, List<RespostaDTO> respostas) {
}
