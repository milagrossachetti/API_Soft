package com.software.API.DTOs;


import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class EvolucionDTO {

    private String texto;

    private PlantillaControlDTO plantillaControl;

    private PlantillaLaboratorioDTO plantillaLaboratorio;

    private List<RecetaDTO> recetas; // Lista de recetas asociadas a la evolución (opcional)

    public EvolucionDTO() {}

    public EvolucionDTO(String texto, PlantillaControlDTO plantillaControl, PlantillaLaboratorioDTO plantillaLaboratorio, List<RecetaDTO> recetas) {
        this.texto = texto;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = recetas;
    }

    public List<RecetaDTO> getRecetas() {
        return recetas != null ? Collections.unmodifiableList(recetas) : Collections.emptyList();
    }

    public void agregarReceta(RecetaDTO receta) {
        if (this.recetas == null) {
            this.recetas = new ArrayList<>();
        }
        this.recetas.add(receta);
    }

}

