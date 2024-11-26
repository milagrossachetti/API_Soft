package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantillaControl {
    private Long id; // Opcional si se utiliza para lógica de negocio

    private Double peso;
    private Double altura;
    private String presion;
    private Integer pulso;
    private Integer saturacion;
    private Double nivelAzucar;

    public PlantillaControl() {}

    public PlantillaControl(Double peso, Double altura, String presion, Integer pulso, Integer saturacion, Double nivelAzucar) {
        this.peso = (peso != null && peso >= 0) ? peso : null;
        this.altura = (altura != null && altura >= 0) ? altura : null;
        this.presion = presion;
        this.pulso = (pulso != null && pulso >= 0) ? pulso : null;
        this.saturacion = (saturacion != null && saturacion >= 0) ? saturacion : null;
        this.nivelAzucar = (nivelAzucar != null && nivelAzucar >= 0) ? nivelAzucar : null;
    }
}

