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
@AllArgsConstructor
public class HistoriaClinica {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaCreacion;

    private List<Diagnostico> diagnosticos = new ArrayList<>();

    // Constructor con validación
    public HistoriaClinica() {
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


    public Evolucion crearYAgregarEvolucion(Long diagnosticoId, String texto, String nombreMedico, String especialidadMedico,
                                            PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio) {
        Diagnostico diagnostico = obtenerDiagnosticoPorId(diagnosticoId);
        return diagnostico.crearYAgregarEvolucion(texto, nombreMedico, especialidadMedico, plantillaControl, plantillaLaboratorio);
    }

    public Receta crearReceta(List<String> medicamentos, Long diagnosticoId, Long evolucionId, String nombreMedico, String especialidadMedico) {
        Diagnostico diagnostico = obtenerDiagnosticoPorId(diagnosticoId);
        return diagnostico.crearReceta(medicamentos, evolucionId, nombreMedico, especialidadMedico);
    }


}
