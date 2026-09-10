package com.algoritmando.controller;

import com.algoritmando.dto.PerguntaResponse;
import com.algoritmando.dto.QuizResponse;
import com.algoritmando.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public List<QuizResponse> listarQuizzes() {
        return quizService.listarQuizzes();
    }

    @GetMapping("/{quizId}/perguntas")
    public List<PerguntaResponse> listarPerguntas(
            @PathVariable Long quizId
    ) {
        return quizService.listarPerguntas(quizId);
    }
}