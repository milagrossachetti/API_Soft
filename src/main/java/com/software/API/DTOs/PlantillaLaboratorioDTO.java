package com.software.API.DTOs;


import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PlantillaLaboratorioDTO {
    @NotNull
    @Size(min = 1, message = "Debe haber al menos un tipo de estudio.")
    private List<String> tiposEstudios;

    @NotNull
    @Size(min = 1, message = "Debe haber al menos un ítem seleccionado.")
    private List<String> items;

    private String estado;

    public PlantillaLaboratorioDTO() {}

    public PlantillaLaboratorioDTO(List<String> tiposEstudios, List<String> items, String estado) {
        this.tiposEstudios = tiposEstudios;
        this.items = items;
        this.estado = estado;
    }
}

