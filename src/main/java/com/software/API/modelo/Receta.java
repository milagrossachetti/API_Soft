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
    private String nombreMedico;
    private String especialidadMedico;// Referencia directa al médico
    private List<MedicamentoRecetado> medicamentos = new ArrayList<>(); // Lista gestionada manualmente
    private boolean anulada;


    public Receta() {}

    public Receta(LocalDateTime fecha, List<MedicamentoRecetado> medicamentos, String nombreMedico, String especialidadMedico) {
        this.fecha = fecha;
        this.medicamentos = medicamentos != null ? medicamentos : new ArrayList<>();
        this.anulada = false;
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
        this.id = System.currentTimeMillis();
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


}
