package com.software.API.DTOs;


import com.software.API.modelo.Rol;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UsuarioDTO {
    @NotNull
    private Long cuil;
    @NotNull
    private String email;
    private String contrasenia;
    private Long matricula; // Solo si es médico
    private String especialidad; // Solo si es médico
    private Long dni;
    private String nombreCompleto;
    private Long telefono;
    private String direccion;
    private String localidad;
    private String provincia;
    private String pais;
    private boolean activo;
}