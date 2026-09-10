package com.algoritmando.service;

import com.algoritmando.dto.PerguntaResponse;
import com.algoritmando.dto.QuizResponse;
import com.algoritmando.model.Pergunta;
import com.algoritmando.model.Quiz;
import com.algoritmando.repository.PerguntaRepository;
import com.algoritmando.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final PerguntaRepository perguntaRepository;

    public List<QuizResponse> listarQuizzes() {
        return quizRepository.findByAtivoTrue()
                .stream()
                .map(this::converterParaQuizResponse)
                .toList();
    }

    public List<PerguntaResponse> listarPerguntas(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                NOT_FOUND,
                                "Quiz não encontrado"
                        )
                );

        if (!quiz.isAtivo()) {
            throw new ResponseStatusException(
                    NOT_FOUND,
                    "Quiz não encontrado"
            );
        }

        return perguntaRepository
                .findByQuizIdOrderByIdAsc(quizId)
                .stream()
                .map(this::converterParaPerguntaResponse)
                .toList();
    }

    private QuizResponse converterParaQuizResponse(Quiz quiz) {
        long quantidadePerguntas =
                perguntaRepository.countByQuizId(quiz.getId());

        return new QuizResponse(
                quiz.getId(),
                quiz.getTitulo(),
                quiz.getDescricao(),
                quiz.getCategoria(),
                quiz.getDificuldade(),
                quantidadePerguntas
        );
    }

    private PerguntaResponse converterParaPerguntaResponse(
            Pergunta pergunta
    ) {
        return new PerguntaResponse(
                pergunta.getId(),
                pergunta.getEnunciado(),
                pergunta.getAlternativaA(),
                pergunta.getAlternativaB(),
                pergunta.getAlternativaC(),
                pergunta.getAlternativaD()
        );
    }
}