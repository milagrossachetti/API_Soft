package com.software.API.excepcion;

/**
 * Excepción lanzada cuando se intenta enviar un correo no válido.
 */
public class CorreoInvalidoException extends RuntimeException {
    public CorreoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
