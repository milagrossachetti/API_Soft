package com.software.API.excepcion;

/**
 * Excepción lanzada cuando no hay un usuario autenticado.
 */
public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException(String mensaje) {
        super(mensaje);
    }
}
