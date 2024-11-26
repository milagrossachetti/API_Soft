package com.software.API.modelo;

import com.software.API.excepcion.DiagnosticoNoEncontradoException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaCreacion;

    private Paciente paciente;

    private List<Diagnostico> diagnosticos = new ArrayList<>();

    // Constructor con validación
    public HistoriaClinica(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente es obligatorio.");
        }
        this.paciente = paciente;
        this.fechaCreacion = LocalDate.now();
    }

    public void agregarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico == null) {
            throw new IllegalArgumentException("El diagnóstico no puede ser nulo.");
        }
        this.diagnosticos.add(diagnostico);
    }

    public List<Diagnostico> obtenerDiagnosticos() {
        return Collections.unmodifiableList(diagnosticos);
    }

    public Diagnostico obtenerDiagnosticoPorId(Long diagnosticoId) {
        return this.diagnosticos.stream()
                .filter(diagnostico -> diagnostico.getId().equals(diagnosticoId))
                .findFirst()
                .orElseThrow(() -> new DiagnosticoNoEncontradoException("Diagnóstico no encontrado con ID: " + diagnosticoId));
    }



}
