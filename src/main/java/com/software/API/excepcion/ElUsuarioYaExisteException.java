package com.software.API.excepcion;

public class ElUsuarioYaExisteException extends RuntimeException {
    public ElUsuarioYaExisteException(String message) {
        super(message);
    }
}
