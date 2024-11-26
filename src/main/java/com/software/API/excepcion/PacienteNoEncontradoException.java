package com.software.API.excepcion;

public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

