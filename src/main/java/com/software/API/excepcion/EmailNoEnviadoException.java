package com.software.API.excepcion;

/**
 * Excepción personalizada para manejar errores al enviar correos electrónicos.
 */
public class EmailNoEnviadoException extends RuntimeException {

    /**
     * Constructor con un mensaje de error.
     *
     * @param mensaje el mensaje de error
     */
    public EmailNoEnviadoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con un mensaje de error y una causa.
     *
     * @param mensaje el mensaje de error
     * @param causa   la causa original de la excepción
     */
    public EmailNoEnviadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
