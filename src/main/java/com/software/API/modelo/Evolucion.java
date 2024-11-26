package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class Evolucion {
    private Long id;

    private LocalDateTime fechaEvolucion;
    private String texto;

    @JsonBackReference
    private Diagnostico diagnostico;

    @JsonIgnore
    private Usuario usuario; // Médico autenticado, obligatorio

    private PlantillaControl plantillaControl;
    private PlantillaLaboratorio plantillaLaboratorio;
    private List<Receta> recetas = new ArrayList<>();

    private String rutaPdf;

    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario) {
        if (texto == null || fechaEvolucion == null || usuario == null) {
            throw new IllegalArgumentException("Texto, fecha y usuario son obligatorios.");
        }
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.usuario = usuario;
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio,
                     List<Receta> recetas, String rutaPdf) {
        this(texto, fechaEvolucion, usuario);
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
