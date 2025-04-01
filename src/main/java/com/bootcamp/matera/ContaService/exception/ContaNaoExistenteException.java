package com.bootcamp.matera.ContaService.exception;

public class ContaNaoExistenteException extends RuntimeException {
    public ContaNaoExistenteException(String message) {

        super(message);
    }
}
