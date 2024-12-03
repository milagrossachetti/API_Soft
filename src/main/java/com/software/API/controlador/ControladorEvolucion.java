package com.software.API.controlador;

import com.software.API.DTOs.EvolucionDTO;
import com.software.API.DTOs.RecetaDTO;
import com.software.API.excepcion.DiagnosticoNoEncontradoException;
import com.software.API.excepcion.PacienteNoEncontradoException;
import com.software.API.modelo.Evolucion;
import com.software.API.servicio.ServicioUsuario;
import com.software.API.servicio.ServicioEvolucion;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated // Para activar validaciones en DTOs
public class ControladorEvolucion {


    private final ServicioEvolucion servicioEvolucion;
    private final ServicioUsuario servicioUsuario;

    public ControladorEvolucion(
            ServicioEvolucion servicioEvolucion,
            ServicioUsuario servicioUsuario) {
        this.servicioEvolucion = servicioEvolucion;
        this.servicioUsuario = servicioUsuario;
    }

    //CREAR EVOLUCION
    @PostMapping("/evoluciones/{cuilPaciente}/{diagnosticoId}")
    public ResponseEntity<Object> agregarEvolucion(
            @PathVariable Long cuilPaciente,
            @PathVariable Long diagnosticoId,
            @RequestBody @Valid EvolucionDTO evolucionDTO) {
        try {
            // Validar preliminarmente que el DTO no sea nulo y tenga contenido
            if (evolucionDTO == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El cuerpo de la evolución no puede ser nulo.");
            }

            boolean tieneContenido = (evolucionDTO.getTexto() != null && !evolucionDTO.getTexto().isEmpty()) ||
                    evolucionDTO.getPlantillaControl() != null ||
                    evolucionDTO.getPlantillaLaboratorio() != null ||
                    (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty());

            if (!tieneContenido) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La evolución debe tener al menos texto, plantilla de control, plantilla de laboratorio o receta.");
            }

            boolean tieneRecetaYLaboratorio = (evolucionDTO.getPlantillaLaboratorio() != null &&
                    (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()));

            if(tieneRecetaYLaboratorio){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La evolucion no puede tener receta y plantilla de laboratorio al mismo tiempo.");
            }

            // Obtener datos del médico autenticado
            String nombreMedico = servicioUsuario.obtenerNombreCompletoMedicoAutenticado();
            String especialidadMedico = servicioUsuario.obtenerEspecialidadMedicoAutenticado();

            // Crear la evolución delegando al servicio
            Evolucion evolucion = servicioEvolucion.crearEvolucion(cuilPaciente, diagnosticoId, evolucionDTO, nombreMedico, especialidadMedico);

            // Responder con el estado 201 (CREATED) y la evolución creada
            return ResponseEntity.status(HttpStatus.CREATED).body(evolucion);

        } catch (PacienteNoEncontradoException | DiagnosticoNoEncontradoException e) {
            // Manejar errores de entidad no encontrada
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Manejar errores de validación
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Manejar errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

}


