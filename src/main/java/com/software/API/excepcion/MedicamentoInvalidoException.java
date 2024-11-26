package com.software.API.excepcion;

/**
 * Excepción lanzada cuando se intenta usar un medicamento no válido.
 */
public class MedicamentoInvalidoException extends RuntimeException {
    public MedicamentoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
