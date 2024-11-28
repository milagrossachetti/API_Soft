package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Diagnostico {
    private Long id;
    private String nombreDiagnostico;
    private String nombreMedico; // Nombre del médico que creó el diagnóstico
    private String especialidadMedico; // Especialidad del médico
    private List<Evolucion> evoluciones = new ArrayList<>();

    // Constructor por defecto
    public Diagnostico() {}

    // Constructor con solo el nombre del diagnóstico
    public Diagnostico(String nombreDiagnostico) {
        if (nombreDiagnostico == null) {
            throw new IllegalArgumentException("El nombre del diagnóstico no puede ser nulo.");
        }
        this.nombreDiagnostico = nombreDiagnostico;
    }

    // Constructor con nombre del diagnóstico, médico y especialidad
    public Diagnostico(String nombreDiagnostico, String nombreMedico, String especialidadMedico) {
        if (nombreDiagnostico == null || nombreMedico == null || especialidadMedico == null) {
            throw new IllegalArgumentException("El nombre del diagnóstico, el nombre del médico y la especialidad no pueden ser nulos.");
        }
        this.nombreDiagnostico = nombreDiagnostico;
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
    }


    // Constructor con nombre, historia clínica y evolución inicial
    public Diagnostico(String nombreDiagnostico, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, String nombreMedico, String especialidadMedico) {
        this(nombreDiagnostico, nombreMedico, especialidadMedico);
        agregarEvolucion(primeraEvolucion);
    }

    // Método para agregar una evolución al diagnóstico
    public void agregarEvolucion(Evolucion evolucion) {
        if (evolucion == null) {
            throw new IllegalArgumentException("La evolución no puede ser nula.");
        }
        this.evoluciones.add(evolucion);
    }

    // Obtener la lista de evoluciones como una lista inmutable
    public List<Evolucion> obtenerEvoluciones() {
        return Collections.unmodifiableList(evoluciones);
    }

    // Método para crear y agregar una evolución
    public Evolucion crearYAgregarEvolucion(String texto, String nombreMedico, String especialidadMedico,
                                            PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio) {

        if (nombreMedico == null || nombreMedico.isEmpty() || especialidadMedico == null || especialidadMedico.isEmpty()) {
            throw new IllegalArgumentException("El nombre y la especialidad del médico son obligatorios.");
        }

        Evolucion nuevaEvolucion = new Evolucion (
                texto,
                LocalDateTime.now(),
                nombreMedico,
                especialidadMedico,
                plantillaControl,
                plantillaLaboratorio,
                new ArrayList<>()
        );

        evoluciones.add(nuevaEvolucion);
        return nuevaEvolucion;
    }


    public Receta crearReceta(List<String> medicamentos, Long evolucionId, String nombreMedico, String especialidadMedico) {
        Evolucion evolucion = this.evoluciones.stream()
                .filter(e -> e.getId().equals(evolucionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Evolución no encontrada con ID: " + evolucionId));

        return evolucion.crearReceta(medicamentos, nombreMedico, especialidadMedico);
    }


}
