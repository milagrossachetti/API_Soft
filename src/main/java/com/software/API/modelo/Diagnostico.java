package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Diagnostico {
    private Long id;
    private HistoriaClinica historiaClinica;

    private String nombre;
    private Usuario usuario;
    private List<Evolucion> evoluciones = new ArrayList<>();

    public Diagnostico() {}

    public Diagnostico(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.evoluciones = new ArrayList<>();
    }

    public Diagnostico(String nombre, HistoriaClinica historiaClinica, Usuario usuario) {
        this.nombre = nombre;
        this.historiaClinica = historiaClinica;
        this.usuario = usuario;
        this.evoluciones = new ArrayList<>();
    }

    public Diagnostico(String nombre, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, Usuario usuario) {
        this(nombre, usuario);
        this.historiaClinica = historiaClinica;
        agregarEvolucion(primeraEvolucion);
    }

    public void agregarEvolucion(Evolucion evolucion) {
        if (evolucion != null) {
            this.evoluciones.add(evolucion);
            evolucion.setDiagnostico(this);
        }
    }

}