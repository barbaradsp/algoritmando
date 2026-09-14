package com.algoritmando.controller;

import com.algoritmando.dto.RankingResponse;
import com.algoritmando.service.ResultadoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class RankingController {

    private final ResultadoService resultadoService;

    @GetMapping("/{quizId}/ranking")
    public ResponseEntity<List<RankingResponse>>
    listarRanking(

            @PathVariable
            Long quizId

    ) {

        return ResponseEntity.ok(
                resultadoService
                        .listarRanking(
                                quizId
                        )
        );
    }
}