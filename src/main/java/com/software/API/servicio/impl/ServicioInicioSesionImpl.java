package com.software.API.servicio.impl;

import com.software.API.modelo.Usuario;
import com.software.API.servicio.ServicioInicioSesion;

public class ServicioInicioSesionImpl implements ServicioInicioSesion {

    @Override
    public String inicioSesion(Usuario usuario, Usuario usuarioInicioSesion){

        if (usuario.getEmail().equals(usuarioInicioSesion.getEmail()) &&
                usuario.getContrasenia().equals(usuarioInicioSesion.getContrasenia())){
            return "Inicio de sesión exitoso.";

        } else if (!usuario.getEmail().equals(usuarioInicioSesion.getEmail())) {
            return "Usuario incorrecto.";

        } else {
            return "Contraseña incorrecta.";
        }
    }

}
