package com.bootcamp.matera.ContaService.exception;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {

      super(message);
    }
}
