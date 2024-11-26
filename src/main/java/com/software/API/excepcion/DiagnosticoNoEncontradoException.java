package com.software.API.excepcion;

public class DiagnosticoNoEncontradoException extends RuntimeException {
    public DiagnosticoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

