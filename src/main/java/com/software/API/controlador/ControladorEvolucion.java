package com.software.API.controlador;

import com.software.API.DTOs.DiagnosticoDTO;
import com.software.API.DTOs.EvolucionDTO;
import com.software.API.excepcion.DiagnosticoNoEncontradoException;
import com.software.API.excepcion.PacienteNoEncontradoException;
import com.software.API.modelo.Diagnostico;
import com.software.API.modelo.Evolucion;
import com.software.API.servicio.ServicioUsuario;
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

    private final ServicioUsuario servicioUsuario;

    public ControladorEvolucion(
            ServicioEvolucion servicioEvolucion,
            ServicioDiagnostico servicioDiagnostico,
            ServicioUsuario servicioUsuario) {
        this.servicioEvolucion = servicioEvolucion;
        this.servicioDiagnostico = servicioDiagnostico;
        this.servicioUsuario = servicioUsuario;
    }


        //CREAR DIAGNOSTICO
        @PostMapping("/diagnosticos/crear-diagnostico")
        public ResponseEntity<Object> crearDiagnostico(@RequestBody @Valid DiagnosticoDTO diagnosticoDTO) {
            try {
    
                // Extraer solo los valores necesarios
                String nombreMedico = servicioUsuario.obtenerNombreCompletoMedicoAutenticado();
                String especialidadMedico = servicioUsuario.obtenerEspecialidadMedicoAutenticado();
    
                // Pasar los valores al servicio
                Diagnostico diagnostico = servicioDiagnostico.crearDiagnostico(
                        diagnosticoDTO.getPacienteDTO().getCuil(),
                        diagnosticoDTO.getNombreDiagnostico(),
                        diagnosticoDTO.getEvolucionDTO(),
                        nombreMedico,
                        especialidadMedico
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

            // Extraer solo los valores necesarios
            String nombreMedico = servicioUsuario.obtenerNombreCompletoMedicoAutenticado();
            String especialidadMedico = servicioUsuario.obtenerEspecialidadMedicoAutenticado();

            // Crear la evolución delegando al servicio
            Evolucion evolucion = servicioEvolucion.crearEvolucion(cuilPaciente, diagnosticoId, evolucionDTO, nombreMedico, especialidadMedico);

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
