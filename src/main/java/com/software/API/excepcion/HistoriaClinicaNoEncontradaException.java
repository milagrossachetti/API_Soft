package com.software.API.excepcion;


public class HistoriaClinicaNoEncontradaException extends RuntimeException {
    public HistoriaClinicaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
