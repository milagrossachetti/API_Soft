package com.software.API.excepcion;

public class RecetaInvalidaException extends RuntimeException {
    public RecetaInvalidaException(String mensaje) {
        super(mensaje);
    }

    public RecetaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
