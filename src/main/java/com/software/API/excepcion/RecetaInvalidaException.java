package com.software.API.excepcion;

public class RecetaInvalidaException extends RuntimeException {
    public RecetaInvalidaException(String message) {
        super(message);
    }

    public RecetaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

}
