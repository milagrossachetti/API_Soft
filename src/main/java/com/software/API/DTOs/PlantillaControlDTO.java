package com.software.API.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PlantillaControlDTO {
    @Min(0)
    private Double peso;

    @Min(0)
    private Double altura;

    @Pattern(regexp = "\\d+/\\d+", message = "El formato debe ser '120/80'.")
    private String presion;

    @Min(0)
    private Integer pulso;

    @Min(0)
    private Integer saturacion;

    @Min(0)
    private Double nivelAzucar;

    public PlantillaControlDTO() {}

    public PlantillaControlDTO(Double peso, Double altura, String presion, Integer pulso, Integer saturacion, Double nivelAzucar) {
        this.peso = peso;
        this.altura = altura;
        this.presion = presion;
        this.pulso = pulso;
        this.saturacion = saturacion;
        this.nivelAzucar = nivelAzucar;
    }
}
