package com.software.API.DTOs;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class PacienteDTO {
    @NotNull(message = "El CUIL del paciente es obligatorio.")
    private Long cuil;

    public PacienteDTO(@NotNull(message = "El CUIL del paciente es obligatorio.") Long cuil) {
        this.cuil = cuil;
    }


}
