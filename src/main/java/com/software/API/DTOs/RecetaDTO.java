package com.software.API.DTOs;


import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RecetaDTO {
    @NotNull
    @Size(max = 2, message = "Solo se permiten hasta dos medicamentos.")
    private List<String> medicamentos;

    private boolean anulado;
    private LocalDateTime fecha;
    private Long idMedico;
    private String nombreMedico;

    public RecetaDTO() {}

    public RecetaDTO(List<String> medicamentos, boolean anulado, LocalDateTime fecha, Long idMedico, String nombreMedico) {
        this.medicamentos = medicamentos;
        this.anulado = anulado;
        this.fecha = fecha;
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
    }

    public List<String> getMedicamentos() {
        return medicamentos != null ? Collections.unmodifiableList(medicamentos) : Collections.emptyList();
    }

    public void agregarMedicamento(String medicamento) {
        if (this.medicamentos == null) {
            this.medicamentos = new ArrayList<>();
        }
        if (this.medicamentos.size() >= 2) {
            throw new IllegalStateException("Solo se permiten hasta dos medicamentos.");
        }
        this.medicamentos.add(medicamento);
    }


}

