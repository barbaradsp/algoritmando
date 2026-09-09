package com.algoritmando.service;

import com.algoritmando.dto.RespostaDTO;
import com.algoritmando.dto.ResultadoRequest;
import com.algoritmando.dto.ResultadoResponse;
import com.algoritmando.model.Pergunta;
import com.algoritmando.model.Resultado;
import com.algoritmando.repository.PerguntaRepository;
import com.algoritmando.repository.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultadoService {

    private static final int PONTOS_POR_ACERTO = 20;

    private final PerguntaRepository perguntaRepository;
    private final ResultadoRepository resultadoRepository;

    public ResultadoResponse calcularResultado(ResultadoRequest request) {
        Map<Long, Pergunta> perguntasPorId = perguntaRepository.findAll().stream()
                .collect(Collectors.toMap(Pergunta::getId, p -> p));

        int acertos = 0;
        for (RespostaDTO resposta : request.respostas()) {
            Pergunta pergunta = perguntasPorId.get(resposta.perguntaId());
            if (pergunta != null && pergunta.getRespostaCorreta().equalsIgnoreCase(resposta.respostaEscolhida())) {
                acertos++;
            }
        }

        int totalPerguntas = request.respostas().size();
        int pontuacao = acertos * PONTOS_POR_ACERTO;

        Resultado resultado = Resultado.builder()
                .nomeUsuario(request.nomeUsuario())
                .pontuacao(pontuacao)
                .acertos(acertos)
                .totalPerguntas(totalPerguntas)
                .build();

        resultado = resultadoRepository.save(resultado);

        return new ResultadoResponse(
                resultado.getId(),
                resultado.getNomeUsuario(),
                resultado.getPontuacao(),
                resultado.getAcertos(),
                resultado.getTotalPerguntas()
        );
    }
}
