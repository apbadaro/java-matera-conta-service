package com.bootcamp.matera.ContaService.configuracao;

import com.bootcamp.matera.ContaService.exception.ContaExistenteException;
import com.bootcamp.matera.ContaService.exception.SaldoInsuficienteException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    // Aqui podemos adicionar métodos para lidar com exceções específicas
    // e retornar respostas personalizadas para o cliente.

    @ExceptionHandler(ContaExistenteException.class)
    private ProblemDetail exceptionContaExistente(ContaExistenteException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Conflict");
        problemDetail.setType(URI.create("http://localhost/9000/doc/conta-existente"));
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Error no DTO");
        problemDetail.setType(URI.create("http://localhost/9000/doc/error-dto"));
        problemDetail.setDetail(errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    private ProblemDetail handlerSaldoInsuficiente(SaldoInsuficienteException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setTitle("Saldo Insuficiente");
        problemDetail.setType(URI.create("http://localhost/9000/document/saldo-insuficiente"));
        return problemDetail;
    }
}
