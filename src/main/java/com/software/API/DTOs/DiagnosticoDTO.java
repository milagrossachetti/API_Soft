package com.software.API.DTOs;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DiagnosticoDTO {
    @NotNull
    private Long idHistoriaClinica;

    @NotNull
    private String nombreDiagnostico;

    @NotNull
    private EvolucionDTO evolucionDTO;

    public DiagnosticoDTO() {}

    public DiagnosticoDTO(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO) {
        this.idHistoriaClinica = idHistoriaClinica;
        this.nombreDiagnostico = nombreDiagnostico;
        this.evolucionDTO = evolucionDTO;
    }
}
