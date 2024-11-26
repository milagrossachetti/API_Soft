package com.software.API.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Usuario {
    @Id
    private Long cuil;
    private String email;
    private String contrasenia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol", nullable = false)
    private Rol rol;
    @Column(nullable = true)
    private Long matricula; // Solo si es médico
    @Column(nullable = true)
    private String especialidad; // Solo si es médico
    private Long dni;
    private String nombreCompleto;
    private Long telefono;
    private String direccion;
    private String localidad;
    private String provincia;
    private String pais;
    private boolean activo;

    public Usuario(Long cuil, String email, String contrasenia, Rol rol, Long matricula, String especialidad, Long dni, String nombreCompleto, Long telefono, String direccion, String localidad, String provincia, String pais) {
        this.cuil = cuil;
        ;this.email = email;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.activo = true;
    }

    public Usuario(Long cuil, String email, String contrasenia, Rol rol, Long dni, String nombreCompleto, Long telefono, String direccion, String localidad, String provincia, String pais) {
        this.cuil = cuil;
        this.email = email;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.activo = true;
    }
}
