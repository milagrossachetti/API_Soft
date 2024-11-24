package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class HistoriaClinica {
    private Long id;
    private Date fechaCreacion;
    private Paciente paciente;
    private List<Diagnostico> diagnosticos;

    public HistoriaClinica(Long id, Date fechaCreacion) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.diagnosticos = List.of(new Diagnostico(1L, "Dengue"), new Diagnostico(2L, "Covid"), new Diagnostico(3L, "Influenza A"),
                new Diagnostico(4L, "Gastroenteritis"), new Diagnostico(5L, "Pulmonia"));
    }

    public HistoriaClinica(Paciente paciente){
        this.paciente= paciente;
        this.fechaCreacion = new Date();
    }
    protected void onCreate() {
        this.fechaCreacion = new Date();
    }
}
