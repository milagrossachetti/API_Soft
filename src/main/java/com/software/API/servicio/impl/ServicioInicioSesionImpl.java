package com.software.API.servicio.impl;

import com.software.API.modelo.Usuario;
import com.software.API.servicio.ServicioInicioSesion;

public class ServicioInicioSesionImpl implements ServicioInicioSesion {

    @Override
    public String inicioSesion(Usuario usuario){

        if ("usuario@dominio.com".equals(usuario.getEmail()) &&
                "123456".equals(usuario.getContrasenia())){
            return "Inicio de sesión exitoso.";

        } else if (!"usuario@dominio.com".equals(usuario.getEmail())) {
            return "Usuario incorrecto.";

        } else {
            return "Contraseña incorrecta.";
        }
    }

}
