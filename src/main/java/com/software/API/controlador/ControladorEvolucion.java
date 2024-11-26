package com.software.API.controlador;

import com.software.API.DTOs.DiagnosticoDTO;
import com.software.API.DTOs.EvolucionDTO;
import com.software.API.excepcion.DiagnosticoNoEncontradoException;
import com.software.API.excepcion.PacienteNoEncontradoException;
import com.software.API.modelo.Diagnostico;
import com.software.API.modelo.Evolucion;
import com.software.API.modelo.Usuario;
import com.software.API.repositorio.RepositorioUsuario;
import com.software.API.servicio.ServicioDiagnostico;
import com.software.API.servicio.ServicioEvolucion;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@Validated // Para activar validaciones en DTOs
public class ControladorEvolucion {


    private final ServicioEvolucion servicioEvolucion;
    private final ServicioDiagnostico servicioDiagnostico;
    private final RepositorioUsuario repositorioUsuario;

    public ControladorEvolucion(
            ServicioEvolucion servicioEvolucion,
            ServicioDiagnostico servicioDiagnostico,
            RepositorioUsuario repositorioUsuario) {
        this.servicioEvolucion = servicioEvolucion;
        this.servicioDiagnostico = servicioDiagnostico;
        this.repositorioUsuario = repositorioUsuario;
    }

    //VERIFICAR AUTENTICACION PARA ESTE METODO NECESARIO PARA CREAR DIAGNOSTICO Y EVOLUCIONES
    private Usuario obtenerMedicoAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un médico autenticado en el contexto actual.");
        }
        String email = authentication.getName();
        return repositorioUsuario.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos."));
    }

    //CREAR DIAGNOSTICO
    @PostMapping("/diagnosticos/crear-diagnostico")
    public ResponseEntity<Object> crearDiagnostico(@RequestBody @Valid DiagnosticoDTO diagnosticoDTO) {
        try {
            Usuario medico = obtenerMedicoAutenticado();
            Diagnostico diagnostico = servicioDiagnostico.crearDiagnostico(
                    diagnosticoDTO.getIdHistoriaClinica(),
                    diagnosticoDTO.getNombreDiagnostico(),
                    diagnosticoDTO.getEvolucionDTO(),
                    medico
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(diagnostico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //CREAR EVOLUCION
    @PostMapping("/pacientes/{cuilPaciente}/diagnosticos/{diagnosticoId}/evoluciones")
    public ResponseEntity<Object> agregarEvolucion(
            @PathVariable Long cuilPaciente,
            @PathVariable Long diagnosticoId,
            @RequestBody @Valid EvolucionDTO evolucionDTO) {
        try {
            // Validación preliminar del DTO
            if (evolucionDTO.getTexto() == null || evolucionDTO.getTexto().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El texto de la evolución no puede estar vacío.");
            }

            // Obtener el médico autenticado
            Usuario medico = obtenerMedicoAutenticado();

            // Crear la evolución delegando al servicio
            Evolucion evolucion = servicioEvolucion.crearEvolucion(cuilPaciente, diagnosticoId, evolucionDTO, medico);

            // Responder con el estado 201 (CREATED) y la evolución creada
            return ResponseEntity.status(HttpStatus.CREATED).body(evolucion);

        } catch (PacienteNoEncontradoException | DiagnosticoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
