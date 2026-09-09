package com.algoritmando.config;

import com.algoritmando.model.Pergunta;
import com.algoritmando.repository.PerguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PerguntaRepository perguntaRepository;

    @Override
    public void run(String... args) {
        if (perguntaRepository.count() > 0) return;

        perguntaRepository.saveAll(java.util.List.of(
                Pergunta.builder()
                        .enunciado("Qual estrutura de repetição é indicada quando não sabemos o número de iterações antecipadamente?")
                        .alternativaA("for")
                        .alternativaB("while")
                        .alternativaC("switch")
                        .alternativaD("if")
                        .respostaCorreta("B")
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual a complexidade de tempo de uma busca binária em um array ordenado?")
                        .alternativaA("O(n)")
                        .alternativaB("O(n²)")
                        .alternativaC("O(log n)")
                        .alternativaD("O(1)")
                        .respostaCorreta("C")
                        .build(),

                Pergunta.builder()
                        .enunciado("Em pseudocódigo, o que representa uma variável do tipo 'flag'?")
                        .alternativaA("Um número decimal")
                        .alternativaB("Um valor booleano de controle")
                        .alternativaC("Uma lista de valores")
                        .alternativaD("Um texto")
                        .respostaCorreta("B")
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual estrutura de dados segue o princípio LIFO (Last In, First Out)?")
                        .alternativaA("Fila")
                        .alternativaB("Lista encadeada")
                        .alternativaC("Pilha")
                        .alternativaD("Árvore")
                        .respostaCorreta("C")
                        .build(),

                Pergunta.builder()
                        .enunciado("O que é um algoritmo recursivo?")
                        .alternativaA("Um algoritmo que nunca termina")
                        .alternativaB("Um algoritmo que chama a si mesmo para resolver subproblemas")
                        .alternativaC("Um algoritmo que só usa laços for")
                        .alternativaD("Um algoritmo sem variáveis")
                        .respostaCorreta("B")
                        .build()
        ));
    }
}