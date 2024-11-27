package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, String nombreMedico, String especialidadMedico) {
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
        this.id = System.currentTimeMillis();
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, String nombreMedico, String especialidadMedico,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio,
                     List<Receta> recetas) {
        this(texto, fechaEvolucion, nombreMedico, especialidadMedico);
        this.nombreMedico = nombreMedico;
        this.especialidadMedico = especialidadMedico;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = (recetas != null) ? recetas : new ArrayList<>();
        this.id = System.currentTimeMillis();
    }


    public Receta crearReceta(List<String> medicamentos, String nombreMedico, String especialidadMedico) {
        if (medicamentos == null || medicamentos.size() > 2) {
            throw new IllegalArgumentException("Solo se permiten hasta 2 medicamentos por receta.");
        }
        if (nombreMedico == null || nombreMedico.isEmpty() || especialidadMedico == null || especialidadMedico.isEmpty()) {
            throw new IllegalArgumentException("El nombre y la especialidad del médico son obligatorios.");
        }

        // Crear los medicamentos recetados
        List<MedicamentoRecetado> listaMedicamentos = medicamentos.stream()
                .map(MedicamentoRecetado::new)
                .collect(Collectors.toList());

        // Crear la receta
        Receta nuevaReceta = new Receta(
                LocalDateTime.now(),
                listaMedicamentos,
                nombreMedico,
                especialidadMedico
        );

        // Agregar la receta a la lista de recetas de la evolución
        this.recetas.add(nuevaReceta);
        return nuevaReceta;
    }

}
