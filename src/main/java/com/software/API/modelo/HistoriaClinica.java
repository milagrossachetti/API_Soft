package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class HistoriaClinica {
    private Long id;
    private LocalDate fechaCreacion;
    private Paciente paciente;
    private List<Diagnostico> diagnosticos;

    public HistoriaClinica(Paciente paciente){
        this.paciente= paciente;
        this.fechaCreacion = LocalDate.now();
    }
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
    }

    public void agregarDiagnostico(Diagnostico diagnostico){
        this.diagnosticos.add(diagnostico);
        diagnostico.setHistoriaClinica(this);
    }
}
