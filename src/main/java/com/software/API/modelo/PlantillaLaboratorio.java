package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlantillaLaboratorio {
    private Long id; // Opcional para lógica de negocio
    private String nombreEstudio; // Nombre del estudio de laboratorio
    private List<String> tiposEstudios = new ArrayList<>();
    private List<String> items = new ArrayList<>();
    private String estado;

    // Constructor por defecto
    public PlantillaLaboratorio() {
        this.estado = "Activo";
    }

    // Constructor con nombre del estudio y lista de ítems
    public PlantillaLaboratorio(String nombreEstudio, List<String> items) {
        if (nombreEstudio == null || nombreEstudio.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estudio no puede ser nulo o vacío.");
        }
        this.nombreEstudio = nombreEstudio;
        this.items = items != null ? items : new ArrayList<>();
        this.estado = "Activo";
    }

    // Constructor con tipos de estudio, ítems y estado
    public PlantillaLaboratorio(List<String> tiposEstudios, List<String> items, String estado) {
        this.tiposEstudios = tiposEstudios != null ? tiposEstudios : new ArrayList<>();
        this.items = items != null ? items : new ArrayList<>();
        this.estado = estado != null ? estado : "Activo";
    }

    // Método para agregar un ítem
    public void agregarItem(String tipoEstudio, String item) {
        if (tipoEstudio != null && item != null) {
            if (!tiposEstudios.contains(tipoEstudio)) {
                tiposEstudios.add(tipoEstudio);
            }
            items.add(item);
        }
    }

    // Método para anular la plantilla
    public void anular() {
        this.estado = "Anulado";
    }
}
