package com.projeto.abilities.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Data;

@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiError(HttpStatusCode status2, String message2, List<String> errors2) {
        super();
        this.status = (HttpStatus) status2;
        this.message = message2;
        errors = errors2;
    }

    public ApiError(HttpStatusCode status, String message, String error) {
        super();
        this.status = (HttpStatus) status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}

// Este é um objeto que representa um erro padrão da API que é retornado como
// resposta em caso de exceções. O objeto contém informações sobre o status da
// resposta HTTP, uma mensagem de erro e uma lista de erros detalhados, caso
// haja mais de um. Ele possui construtores para diferentes tipos de parâmetros,
// incluindo a possibilidade de fornecer apenas uma única mensagem de erro ou
// uma lista de mensagens de erro. Ele também usa o Lombok para gerar
// automaticamente os métodos getters e setters.