package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObraSocial {
    private Long id;
    private String nombre;
    private String codigo;

    public ObraSocial() {}

    public ObraSocial(Long id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }
}