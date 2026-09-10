package com.algoritmando.config;

import com.algoritmando.model.Categoria;
import com.algoritmando.model.Dificuldade;
import com.algoritmando.model.Pergunta;
import com.algoritmando.model.Quiz;
import com.algoritmando.repository.PerguntaRepository;
import com.algoritmando.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PerguntaRepository perguntaRepository;
    private final QuizRepository quizRepository;

    @Override
    public void run(String... args) {

        if (quizRepository.count() > 0) {
            return;
        }

        Quiz estruturasDeDados = quizRepository.save(
                Quiz.builder()
                        .titulo("Estruturas de Dados")
                        .descricao("Teste seus conhecimentos sobre arrays, listas, pilhas e filas.")
                        .categoria(Categoria.ESTRUTURAS_DE_DADOS)
                        .dificuldade(Dificuldade.FACIL)
                        .ativo(true)
                        .build()
        );

        Quiz algoritmos = quizRepository.save(
                Quiz.builder()
                        .titulo("Algoritmos")
                        .descricao("Teste seus conhecimentos sobre busca, ordenação e recursão.")
                        .categoria(Categoria.ALGORITMOS)
                        .dificuldade(Dificuldade.MEDIO)
                        .ativo(true)
                        .build()
        );

        Quiz complexidade = quizRepository.save(
                Quiz.builder()
                        .titulo("Complexidade de Algoritmos")
                        .descricao("Teste seus conhecimentos sobre análise de complexidade e notação Big O.")
                        .categoria(Categoria.COMPLEXIDADE)
                        .dificuldade(Dificuldade.MEDIO)
                        .ativo(true)
                        .build()
        );

        salvarPerguntasEstruturasDeDados(estruturasDeDados);
        salvarPerguntasAlgoritmos(algoritmos);
        salvarPerguntasComplexidade(complexidade);
    }

    private void salvarPerguntasEstruturasDeDados(Quiz quiz) {

        perguntaRepository.saveAll(List.of(

                Pergunta.builder()
                        .enunciado("Qual estrutura de dados segue o princípio LIFO (Last In, First Out)?")
                        .alternativaA("Fila")
                        .alternativaB("Lista")
                        .alternativaC("Pilha")
                        .alternativaD("Árvore")
                        .respostaCorreta("C")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual estrutura de dados segue o princípio FIFO (First In, First Out)?")
                        .alternativaA("Fila")
                        .alternativaB("Pilha")
                        .alternativaC("Árvore")
                        .alternativaD("Grafo")
                        .respostaCorreta("A")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual é a complexidade média para acessar um elemento de um array diretamente pelo índice?")
                        .alternativaA("O(n)")
                        .alternativaB("O(1)")
                        .alternativaC("O(log n)")
                        .alternativaD("O(n²)")
                        .respostaCorreta("B")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Em uma lista simplesmente encadeada, o que cada nó normalmente armazena?")
                        .alternativaA("Somente o índice do elemento")
                        .alternativaB("Somente o valor do elemento")
                        .alternativaC("Um valor e uma referência para o próximo nó")
                        .alternativaD("Uma referência obrigatória para todos os outros nós")
                        .respostaCorreta("C")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual operação remove o elemento que está no topo de uma pilha?")
                        .alternativaA("push")
                        .alternativaB("pop")
                        .alternativaC("enqueue")
                        .alternativaD("insert")
                        .respostaCorreta("B")
                        .quiz(quiz)
                        .build()
        ));
    }

    private void salvarPerguntasAlgoritmos(Quiz quiz) {

        perguntaRepository.saveAll(List.of(

                Pergunta.builder()
                        .enunciado("Para que a busca binária possa ser aplicada corretamente, qual condição normalmente deve ser satisfeita?")
                        .alternativaA("Os elementos devem ser todos diferentes")
                        .alternativaB("Os elementos devem estar ordenados")
                        .alternativaC("A estrutura deve possuir apenas números")
                        .alternativaD("A quantidade de elementos deve ser par")
                        .respostaCorreta("B")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual é a complexidade de tempo da busca binária?")
                        .alternativaA("O(n)")
                        .alternativaB("O(n²)")
                        .alternativaC("O(log n)")
                        .alternativaD("O(1)")
                        .respostaCorreta("C")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Como o Bubble Sort organiza os elementos de uma coleção?")
                        .alternativaA("Compara elementos adjacentes e os troca quando estão fora de ordem")
                        .alternativaB("Divide obrigatoriamente a coleção em duas metades")
                        .alternativaC("Insere todos os elementos em uma árvore")
                        .alternativaD("Busca diretamente a posição final de todos os elementos")
                        .respostaCorreta("A")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual é a principal função do caso base em um algoritmo recursivo?")
                        .alternativaA("Aumentar a quantidade de chamadas recursivas")
                        .alternativaB("Ordenar os dados antes da recursão")
                        .alternativaC("Fazer o algoritmo executar em tempo constante")
                        .alternativaD("Definir quando as chamadas recursivas devem parar")
                        .respostaCorreta("D")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual é a complexidade de tempo no pior caso de uma busca linear em uma lista com n elementos?")
                        .alternativaA("O(n)")
                        .alternativaB("O(log n)")
                        .alternativaC("O(1)")
                        .alternativaD("O(n²)")
                        .respostaCorreta("A")
                        .quiz(quiz)
                        .build()
        ));
    }

    private void salvarPerguntasComplexidade(Quiz quiz) {

        perguntaRepository.saveAll(List.of(

                Pergunta.builder()
                        .enunciado("O que significa dizer que um algoritmo possui complexidade O(1)?")
                        .alternativaA("O algoritmo executa apenas uma vez")
                        .alternativaB("O algoritmo funciona somente com um elemento")
                        .alternativaC("O número de operações não cresce proporcionalmente ao tamanho da entrada")
                        .alternativaD("O algoritmo não utiliza memória")
                        .respostaCorreta("C")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Dois laços de repetição aninhados, cada um percorrendo n elementos, geralmente resultam em qual complexidade?")
                        .alternativaA("O(n)")
                        .alternativaB("O(n²)")
                        .alternativaC("O(log n)")
                        .alternativaD("O(1)")
                        .respostaCorreta("B")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Para entradas suficientemente grandes, qual das complexidades abaixo apresenta melhor crescimento?")
                        .alternativaA("O(n log n)")
                        .alternativaB("O(n²)")
                        .alternativaC("O(n³)")
                        .alternativaD("O(2^n)")
                        .respostaCorreta("A")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("O que a complexidade de espaço de um algoritmo procura analisar?")
                        .alternativaA("A quantidade de linhas do código")
                        .alternativaB("O tempo necessário para compilar o programa")
                        .alternativaC("A velocidade do processador utilizado")
                        .alternativaD("A quantidade de memória utilizada em função do tamanho da entrada")
                        .respostaCorreta("D")
                        .quiz(quiz)
                        .build(),

                Pergunta.builder()
                        .enunciado("Qual das complexidades abaixo representa crescimento exponencial?")
                        .alternativaA("O(log n)")
                        .alternativaB("O(n)")
                        .alternativaC("O(n²)")
                        .alternativaD("O(2^n)")
                        .respostaCorreta("D")
                        .quiz(quiz)
                        .build()
        ));
    }
}