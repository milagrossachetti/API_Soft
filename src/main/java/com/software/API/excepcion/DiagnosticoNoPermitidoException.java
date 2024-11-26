package com.software.API.excepcion;

public class DiagnosticoNoPermitidoException extends RuntimeException {
    public DiagnosticoNoPermitidoException(String mensaje) {
        super(mensaje);
    }
}
