package com.algoritmando.service;

import com.algoritmando.dto.*;

import com.algoritmando.model.Pergunta;
import com.algoritmando.model.Quiz;
import com.algoritmando.model.Resultado;
import com.algoritmando.model.Usuario;

import com.algoritmando.repository.PerguntaRepository;
import com.algoritmando.repository.QuizRepository;
import com.algoritmando.repository.ResultadoRepository;
import com.algoritmando.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ResultadoService {

    private final PerguntaRepository perguntaRepository;

    private final ResultadoRepository resultadoRepository;

    private final QuizRepository quizRepository;

    private final UsuarioRepository usuarioRepository;


    @Transactional
    public ResultadoResponse calcularResultado(
            ResultadoRequest request
    ) {

        if (request.usuarioId() == null) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Usuário não informado"
            );
        }


        if (request.quizId() == null) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Quiz não informado"
            );
        }


        if (
                request.respostas() == null ||
                        request.respostas().isEmpty()
        ) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Nenhuma resposta foi informada"
            );
        }


        Usuario usuario =
                usuarioRepository
                        .findById(
                                request.usuarioId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        NOT_FOUND,
                                        "Usuário não encontrado"
                                )
                        );


        Quiz quiz =
                quizRepository
                        .findById(
                                request.quizId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        NOT_FOUND,
                                        "Quiz não encontrado"
                                )
                        );


        List<Pergunta> perguntas =
                perguntaRepository
                        .findByQuizIdOrderByIdAsc(
                                quiz.getId()
                        );


        if (perguntas.isEmpty()) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "O quiz não possui perguntas"
            );
        }


        Map<Long, Pergunta> perguntasPorId =
                perguntas
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Pergunta::getId,
                                        Function.identity()
                                )
                        );


        validarRespostas(
                request.respostas(),
                perguntasPorId.keySet(),
                perguntas.size()
        );


        Map<Long, String> respostasPorPergunta =
                request.respostas()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        RespostaDTO::perguntaId,

                                        resposta ->
                                                resposta
                                                        .respostaEscolhida()
                                                        .trim()
                                                        .toUpperCase()
                                )
                        );


        int acertos = 0;


        List<RevisaoResponse> revisao =
                new ArrayList<>();


        for (Pergunta pergunta : perguntas) {

            String respostaEscolhida =
                    respostasPorPergunta.get(
                            pergunta.getId()
                    );


            String respostaCorreta =
                    pergunta
                            .getRespostaCorreta()
                            .trim()
                            .toUpperCase();


            boolean correta =
                    respostaCorreta.equals(
                            respostaEscolhida
                    );


            if (correta) {
                acertos++;
            }


            revisao.add(
                    new RevisaoResponse(

                            pergunta.getId(),

                            pergunta.getEnunciado(),

                            respostaEscolhida,

                            obterTextoAlternativa(
                                    pergunta,
                                    respostaEscolhida
                            ),

                            respostaCorreta,

                            obterTextoAlternativa(
                                    pergunta,
                                    respostaCorreta
                            ),

                            correta
                    )
            );
        }


        int totalPerguntas =
                perguntas.size();


        int pontuacao =
                (int) Math.round(
                        (
                                (double) acertos /
                                        totalPerguntas
                        ) * 100
                );


        Resultado resultado =
                Resultado.builder()

                        .usuario(usuario)

                        .quiz(quiz)

                        .pontuacao(
                                pontuacao
                        )

                        .acertos(
                                acertos
                        )

                        .totalPerguntas(
                                totalPerguntas
                        )

                        .build();


        resultado =
                resultadoRepository.save(
                        resultado
                );


        return new ResultadoResponse(

                resultado.getId(),

                usuario.getId(),

                usuario.getNome(),

                usuario.getEmail(),

                quiz.getId(),

                quiz.getTitulo(),

                resultado.getPontuacao(),

                resultado.getAcertos(),

                resultado.getTotalPerguntas(),

                revisao
        );
    }


    @Transactional(readOnly = true)
    public List<HistoricoResultadoResponse>
    listarHistorico(
            String email
    ) {

        if (
                email == null ||
                        email.isBlank()
        ) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "E-mail não informado"
            );
        }


        Usuario usuario =
                usuarioRepository
                        .findByEmailIgnoreCase(
                                email.trim()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        NOT_FOUND,
                                        "Usuário não encontrado"
                                )
                        );


        return resultadoRepository
                .findByUsuario_IdOrderByDataRealizacaoDesc(
                        usuario.getId()
                )
                .stream()
                .map(
                        resultado ->
                                new HistoricoResultadoResponse(

                                        resultado.getId(),

                                        resultado
                                                .getQuiz()
                                                .getId(),

                                        resultado
                                                .getQuiz()
                                                .getTitulo(),

                                        resultado
                                                .getQuiz()
                                                .getCategoria(),

                                        resultado
                                                .getQuiz()
                                                .getDificuldade(),

                                        resultado
                                                .getPontuacao(),

                                        resultado
                                                .getAcertos(),

                                        resultado
                                                .getTotalPerguntas(),

                                        resultado
                                                .getDataRealizacao()
                                )
                )
                .toList();
    }


    private void validarRespostas(
            List<RespostaDTO> respostas,
            Set<Long> idsPerguntasDoQuiz,
            int totalPerguntas
    ) {

        if (
                respostas
                        .stream()
                        .anyMatch(
                                Objects::isNull
                        )
        ) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Resposta inválida"
            );
        }


        if (
                respostas.size() !=
                        totalPerguntas
        ) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Todas as perguntas devem ser respondidas"
            );
        }


        boolean possuiRespostaInvalida =
                respostas
                        .stream()
                        .anyMatch(
                                resposta ->

                                        resposta.perguntaId()
                                                == null ||

                                                resposta.respostaEscolhida()
                                                        == null ||

                                                !Set.of(
                                                        "A",
                                                        "B",
                                                        "C",
                                                        "D"
                                                ).contains(

                                                        resposta
                                                                .respostaEscolhida()
                                                                .trim()
                                                                .toUpperCase()
                                                )
                        );


        if (possuiRespostaInvalida) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Existe uma resposta inválida"
            );
        }


        Set<Long> idsRecebidos =
                respostas
                        .stream()
                        .map(
                                RespostaDTO::perguntaId
                        )
                        .collect(
                                Collectors.toSet()
                        );


        if (
                idsRecebidos.size() !=
                        respostas.size()
        ) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Existem respostas duplicadas"
            );
        }


        boolean possuiPerguntaDeOutroQuiz =
                idsRecebidos
                        .stream()
                        .anyMatch(
                                id ->
                                        !idsPerguntasDoQuiz
                                                .contains(id)
                        );


        if (possuiPerguntaDeOutroQuiz) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Existe uma resposta que não pertence ao quiz selecionado"
            );
        }
    }


    private String obterTextoAlternativa(
            Pergunta pergunta,
            String alternativa
    ) {

        return switch (
                alternativa.toUpperCase()
                ) {

            case "A" ->
                    pergunta.getAlternativaA();

            case "B" ->
                    pergunta.getAlternativaB();

            case "C" ->
                    pergunta.getAlternativaC();

            case "D" ->
                    pergunta.getAlternativaD();

            default ->
                    "";
        };
    }

    @Transactional(readOnly = true)
    public List<RankingResponse> listarRanking(
            Long quizId
    ) {

        if (quizId == null) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Quiz não informado"
            );
        }

        quizRepository
                .findById(quizId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                NOT_FOUND,
                                "Quiz não encontrado"
                        )
                );

        List<Resultado> resultados =
                resultadoRepository
                        .findByQuiz_IdOrderByPontuacaoDescAcertosDescDataRealizacaoAsc(
                                quizId
                        );

        Set<Long> usuariosAdicionados =
                new HashSet<>();

        List<RankingResponse> ranking =
                new ArrayList<>();

        int posicao = 1;

        for (Resultado resultado : resultados) {

            Long usuarioId =
                    resultado
                            .getUsuario()
                            .getId();

            if (
                    usuariosAdicionados.add(
                            usuarioId
                    )
            ) {

                ranking.add(
                        new RankingResponse(

                                posicao,

                                usuarioId,

                                resultado
                                        .getUsuario()
                                        .getNome(),

                                resultado
                                        .getPontuacao(),

                                resultado
                                        .getAcertos(),

                                resultado
                                        .getTotalPerguntas(),

                                resultado
                                        .getDataRealizacao()
                        )
                );

                posicao++;

                if (ranking.size() == 10) {
                    break;
                }
            }
        }

        return ranking;
    }
}