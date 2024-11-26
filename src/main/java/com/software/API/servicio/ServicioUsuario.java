package com.software.API.servicio;


import com.software.API.DTOs.UsuarioDTO;
import com.software.API.modelo.Usuario;

import java.util.Optional;

public interface ServicioUsuario {
    Usuario crearUsuario(UsuarioDTO usuario) throws Exception;
    void eliminarUsuario(Long cuil) throws Exception;
    Usuario buscarUsuario(Long cuil);
    String obtenerNombreCompletoMedicoAutenticado();
    String obtenerEspecialidadMedicoAutenticado();
}