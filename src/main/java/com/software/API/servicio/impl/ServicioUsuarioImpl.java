package com.software.API.servicio.impl;


/*
Interfaz (Interface): Define el contrato o los métodos que deben ser implementados. No contiene la
lógica de negocio, solo las firmas de los métodos. Por ejemplo, UserService define los
métodos como createUser, deleteUser, y verifyMedicalLicense.

Implementación (Impl): Es la clase que proporciona
la lógica específica para los métodos definidos en la
interfaz. En el caso de UserServiceImpl, esta clase implementa
los métodos createUser, deleteUser, y verifyMedicalLicense con
la lógica de negocio real que realiza esas operaciones.
*/

import com.software.API.DTOs.UsuarioDTO;
import com.software.API.controlador.RespuestaAPI;
import com.software.API.excepcion.ElUsuarioYaExisteException;
import com.software.API.modelo.Rol;
import com.software.API.modelo.Usuario;
import com.software.API.repositorio.RepositorioRol;
import com.software.API.repositorio.RepositorioUsuario;
import com.software.API.servicio.ServicioAPISalud;
import com.software.API.servicio.ServicioUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    @Autowired
    RepositorioUsuario repositorioUsuario;

    @Autowired
    RepositorioRol repositorioRol;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ServicioAPISalud servicioAPISalud;

    @Override
    @Transactional
    public Usuario crearUsuario(UsuarioDTO usuarioDTO){
        if (repositorioUsuario.existsByEmail(usuarioDTO.getEmail()) || repositorioUsuario.existsById(usuarioDTO.getCuil())){
            throw new ElUsuarioYaExisteException("El usuario ya existe en el sistema, corrobore su email o cuil.");
        }
        String rolNombre = (usuarioDTO.getEspecialidad() == null && usuarioDTO.getMatricula() == null) ? "RECEPCIONISTA" : "MEDICO";

        Rol rol = repositorioRol.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol '" + rolNombre + "' no encontrado"));

        Usuario usuario = new Usuario(usuarioDTO.getCuil(),
                usuarioDTO.getEmail(),
                passwordEncoder.encode(usuarioDTO.getContrasenia()),
                rol,
                usuarioDTO.getMatricula(),
                usuarioDTO.getEspecialidad(),
                usuarioDTO.getDni(),
                usuarioDTO.getNombreCompleto(),
                usuarioDTO.getTelefono(),
                usuarioDTO.getDireccion(),
                usuarioDTO.getLocalidad(),
                usuarioDTO.getProvincia(),
                usuarioDTO.getPais());
        repositorioUsuario.save(usuario);

         return usuario;
    }


    @Override
    public void eliminarUsuario(Long cuil) throws Exception {
        // Buscar el usuario por su CUIL
        Optional<Usuario> usuario = repositorioUsuario.findByCuil(cuil);

        if (usuario.isPresent()) {
            // Eliminar el usuario si existe
            repositorioUsuario.delete(usuario.get());
        } else {
            // Lanzar excepción si el usuario no existe
            throw new Exception("El usuario no existe en el sistema.");
        }
    }

    @Override
    public Usuario buscarUsuario(Long cuil) {
        return repositorioUsuario.findByCuil(cuil).orElse(null);
    }

    @Override
    public String obtenerNombreCompletoMedicoAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un médico autenticado en el contexto actual.");
        }
        String email = authentication.getName();
        Usuario medico = repositorioUsuario.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos."));
        return medico.getNombreCompleto(); // Solo devolvemos el nombre completo
    }

    @Override
    public String obtenerEspecialidadMedicoAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un médico autenticado en el contexto actual.");
        }
        String email = authentication.getName();
        Usuario medico = repositorioUsuario.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos."));
        return medico.getEspecialidad(); // Solo devolvemos la especialidad
    }

}