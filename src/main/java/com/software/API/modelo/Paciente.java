package com.software.API.modelo;

import com.software.API.excepcion.HistoriaClinicaNoEncontradaException;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Paciente {
    private Long cuil;
    private Long dni;
    private String nombreCompleto;
    private Date fechaNacimiento;
    private String numeroTelefono;
    private String email;
    private String direccion;
    private String localidad;
    private String provincia;
    private String pais;
    private String nroAfiliado;
    private Estado estado;
    private Long obraSocialId;
    private HistoriaClinica historiaClinica; // Se gestiona manualmente, no por JPA.

    public Paciente() {}

    public Paciente(Long cuil, Long dni, String nombreCompleto, Date fechaNacimiento, String numeroTelefono,
                    String email, String direccion, String localidad, String provincia, String pais,
                    String nroAfiliado, Long obraSocialId) {
        if (cuil == null || nombreCompleto == null || fechaNacimiento == null) {
            throw new IllegalArgumentException("CUIL, nombre completo y fecha de nacimiento son obligatorios.");
        }
        this.cuil = cuil;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.nroAfiliado = nroAfiliado;
        this.obraSocialId = obraSocialId;
        this.estado = Estado.ACTIVO;
    }

    public boolean tieneHistoriaClinica() {
        return this.historiaClinica != null;
    }

    public HistoriaClinica obtenerHistoriaClinica() {
        if (this.historiaClinica == null) {
            throw new HistoriaClinicaNoEncontradaException("El paciente no tiene una historia clínica asociada.");
        }
        return this.historiaClinica;
    }

    public void agregarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico == null) {
            throw new IllegalArgumentException("El diagnóstico no puede ser nulo.");
        }
        this.historiaClinica.agregarDiagnostico(diagnostico);
    }

    public Diagnostico obtenerDiagnosticoPorId( Long diagnosticoId) {
        if (!this.tieneHistoriaClinica()) {
            throw new HistoriaClinicaNoEncontradaException("El paciente no tiene una historia clínica asociada.");
        }
        return this.historiaClinica.obtenerDiagnosticoPorId(diagnosticoId);
    }

}
