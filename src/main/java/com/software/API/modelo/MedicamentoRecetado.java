package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicamentoRecetado {

    private String nombre;

    public MedicamentoRecetado() {}

    public MedicamentoRecetado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
