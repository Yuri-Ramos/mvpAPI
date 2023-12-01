package com.projeto.abilities.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String UNPROCESSABLE_ENTITY_MESSAGE = "Unprocessable Entity";

  @ExceptionHandler({ DataIntegrityViolationException.class, MethodArgumentTypeMismatchException.class,
      AbilityException.class, RefreshTokenException.class, AccountException.class, UserException.class })
  public ResponseEntity<ApiError> handleExceptions(RuntimeException ex) {
    log.error("Error: ", ex);
    return new ResponseEntity<>(
        new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, UNPROCESSABLE_ENTITY_MESSAGE, ex.getLocalizedMessage()),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      org.springframework.http.HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .collect(Collectors.toList());
    ApiError apiError = new ApiError(status, "Validation Error", errors);
    return new ResponseEntity<>(apiError, headers, status);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {
    ApiError apiError = new ApiError(status, "Malformed JSON request", ex.getLocalizedMessage());
    return new ResponseEntity<>(apiError, headers, status);
  }

}

// Esse código implementa um manipulador de exceções personalizado para lidar
// com exceções lançadas durante a execução de uma API RESTful. Ele estende a
// classe ResponseEntityExceptionHandler e define vários métodos para manipular
// diferentes tipos de exceções. Além disso, ele também define um método para
// lidar com exceções personalizadas e retornar uma resposta de erro adequada.
// Em resumo, esse código fornece um mecanismo para tratar e retornar respostas
// de erro adequadas em uma API RESTful.