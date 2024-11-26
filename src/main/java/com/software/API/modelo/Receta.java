package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Receta {
    private Long id; // Opcional para lógica en memoria, no para persistencia
    private LocalDateTime fecha;
    private Usuario medico; // Referencia directa al médico
    private List<MedicamentoRecetado> medicamentos = new ArrayList<>(); // Lista gestionada manualmente
    private boolean anulada;
    private Evolucion evolucion; // Referencia directa a la evolución
    private String rutaPdf;

    public Receta() {}

    public Receta(LocalDateTime fecha, Usuario medico, List<MedicamentoRecetado> medicamentos, Evolucion evolucion, String rutaPdf) {
        this.fecha = fecha;
        this.medico = medico;
        this.medicamentos = medicamentos != null ? medicamentos : new ArrayList<>();
        this.evolucion = evolucion;
        this.anulada = false;
        this.rutaPdf = rutaPdf;
    }

    public void anular() {
        this.anulada = true;
    }

    // Métodos adicionales para manipular medicamentos
    public void agregarMedicamento(MedicamentoRecetado medicamento) {
        if (medicamento != null) {
            medicamentos.add(medicamento);
        }
    }

    public void eliminarMedicamento(MedicamentoRecetado medicamento) {
        medicamentos.remove(medicamento);
    }

    public void asignarRutaPdf(String rutaPdf) {
        if (rutaPdf == null || rutaPdf.isEmpty()) {
            throw new IllegalArgumentException("La ruta del PDF no puede ser nula o vacía.");
        }
        this.rutaPdf = rutaPdf;
    }

}
