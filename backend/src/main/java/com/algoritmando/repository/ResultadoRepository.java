package com.algoritmando.repository;

import com.algoritmando.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoRepository
        extends JpaRepository<Resultado, Long> {

    List<Resultado>
    findByUsuario_IdOrderByDataRealizacaoDesc(
            Long usuarioId
    );


    List<Resultado>
    findByQuiz_IdOrderByPontuacaoDescAcertosDescDataRealizacaoAsc(
            Long quizId
    );
}