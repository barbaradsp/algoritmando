package com.algoritmando.repository;

import com.algoritmando.model.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    List<Pergunta> findByQuizIdOrderByIdAsc(Long quizId);

    long countByQuizId(Long quizId);
}