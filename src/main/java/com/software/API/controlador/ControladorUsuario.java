package com.software.API.controlador;


import com.software.API.DTOs.UsuarioDTO;
import com.software.API.DTOs.UsuarioInicioSesionDTO;
import com.software.API.excepcion.ElUsuarioYaExisteException;
import com.software.API.modelo.Paciente;
import com.software.API.modelo.Rol;
import com.software.API.repositorio.RepositorioRol;
import com.software.API.repositorio.RepositorioUsuario;
import com.software.API.servicio.ServicioUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import com.software.API.modelo.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class ControladorUsuario {
    @Autowired
    ServicioUsuario servicioUsuario;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @PostMapping("/registro")
    public ResponseEntity<RespuestaAPI<Usuario>> registro(@RequestBody UsuarioDTO usuarioDTO) throws Exception {
        RespuestaAPI<Usuario> respuesta = new RespuestaAPI<>(servicioUsuario.crearUsuario(usuarioDTO), "Usuario creado con éxito");
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioInicioSesionDTO usuarioInicioSesionDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(usuarioInicioSesionDTO.getEmail(), usuarioInicioSesionDTO.getContrasenia());
            Authentication authenticationRequest = authenticationManager.authenticate(token);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationRequest);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            return ResponseEntity.ok().body("Usuario autenticado");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas. Verifica tu email y contraseña.");
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> obtenerUsuarioAutenticado(){
        Map<String, String> response = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        response.put("email", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/{cuil}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long cuil) {
        Usuario buscarUsuario = servicioUsuario.buscarUsuario(cuil);
        return ResponseEntity.ok(buscarUsuario);
    }
}