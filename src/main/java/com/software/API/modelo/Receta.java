package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Receta {
    private Long id;
    private LocalDateTime fecha;
    private Usuario medico;
    private List<Medicamento> medicamentos;

    private boolean anulada;
    private Evolucion evolucion;


    private String rutaPdf; // Ruta del archivo PDF generado para esta receta

    public Receta() {}

    public Receta(LocalDateTime fecha, Usuario medico, List<Medicamento> medicamentos, Evolucion evolucion) {
        this.fecha = fecha;
        this.medico = medico;
        this.medicamentos = medicamentos;
        this.evolucion = evolucion;
        this.anulada = false;
    }

    public void anular() {
        this.anulada = true;
    }
}
