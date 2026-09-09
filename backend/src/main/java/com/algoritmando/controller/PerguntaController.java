package com.algoritmando.controller;

import com.algoritmando.dto.PerguntaResponse;
import com.algoritmando.repository.PerguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/perguntas")
@RequiredArgsConstructor
public class PerguntaController {

    private final PerguntaRepository perguntaRepository;

    @GetMapping
    public List<PerguntaResponse> listar() {
        return perguntaRepository.findAll().stream()
                .map(p -> new PerguntaResponse(
                        p.getId(),
                        p.getEnunciado(),
                        p.getAlternativaA(),
                        p.getAlternativaB(),
                        p.getAlternativaC(),
                        p.getAlternativaD()
                ))
                .toList();
    }
}
