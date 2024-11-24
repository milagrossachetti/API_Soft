package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Evolucion {
    private Long id;

    private LocalDateTime fechaEvolucion;
    private String texto;
    private Diagnostico diagnostico;
    private Usuario usuario;
    private PlantillaControl plantillaControl;
    private PlantillaLaboratorio plantillaLaboratorio;
    private List<Receta> recetas;

    private String rutaPdf;

    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario) {
        this(texto, fechaEvolucion, usuario, null, null, null, null);
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio,
                     List<Receta> recetas, String rutaPdf) {
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.usuario = usuario;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = recetas != null ? recetas : new ArrayList<>();
        this.rutaPdf = rutaPdf;
    }

    public void anularPlantillaLaboratorio() {
        if (plantillaLaboratorio != null) {
            plantillaLaboratorio.anular();
        }
    }

    public void anularReceta(Receta receta) {
        if (receta != null) {
            receta.anular();
        }
    }
}
