package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantillaControl {
    private Long id;
    private Double peso;
    private Double altura;
    private String presion;
    private Integer pulso;
    private Integer saturacion;
    private Double nivelAzucar;

    public PlantillaControl() {
    }

    public PlantillaControl(Double peso, Double altura, String presion, Integer pulso, Integer saturacion, Double nivelAzucar) {

        this.peso = peso;
        this.altura = altura;
        this.presion = presion;
        this.pulso = pulso;
        this.saturacion = saturacion;
        this.nivelAzucar = nivelAzucar;
    }
}
