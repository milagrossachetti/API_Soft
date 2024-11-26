package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Evolucion {
    private Long id;
    private LocalDateTime fechaEvolucion;
    private String texto;

    @JsonBackReference
    private Diagnostico diagnostico;

    private String nombreMedico; // Nombre del médico asociado a la evolución
    private String especialidadMedico; // Especialidad del médico

    private PlantillaControl plantillaControl;
    private PlantillaLaboratorio plantillaLaboratorio;
    private List<Receta> recetas = new ArrayList<>();

    private String rutaPdf;

    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, String nombreMedico, String especialidadMedico) {
        if (texto == null || fechaEvolucion == null || nombreMedico == null || especialidadMedico == null) {
            throw new IllegalArgumentException("Texto, fecha, nombre del médico y especialidad son obligatorios.");
        }
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, String nombreMedico, String especialidadMedico,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio,
                     List<Receta> recetas, String rutaPdf) {
        this(texto, fechaEvolucion, nombreMedico, especialidadMedico);
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = (recetas != null) ? recetas : new ArrayList<>();
        this.rutaPdf = rutaPdf;
    }

    public void agregarReceta(Receta receta) {
        if (receta == null) {
            throw new IllegalArgumentException("La receta no puede ser nula.");
        }
        this.recetas.add(receta);
    }
}
