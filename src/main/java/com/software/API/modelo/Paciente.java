package com.software.API.modelo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
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
    private HistoriaClinica historiaClinica;

    public Paciente(Long cuil, Long dni, String nombreCompleto, Date fechaNacimiento, String numeroTelefono, String email, String direccion, String localidad, String provincia, String pais, String nroAfiliado, Long obraSocialId) {
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
        this.historiaClinica = new HistoriaClinica(1L, new Date());
        this.estado = Estado.ACTIVO;
    }

    public void modificarPaciente(Long dni, String nombreCompleto, Date fechaNacimiento, String numeroTelefono, String email, String direccion, String localidad, String provincia, String pais, String nroAfiliado, Long obraSocialId){
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
    }

    public void bajaPaciente(){
        this.estado = Estado.SUSPENDIDO;
    }

    public Paciente() {
    }
}
