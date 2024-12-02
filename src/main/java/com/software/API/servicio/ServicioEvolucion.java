package com.software.API.servicio;

import com.software.API.DTOs.EvolucionDTO;
import com.software.API.modelo.Evolucion;
import com.software.API.modelo.Paciente;
import com.software.API.modelo.Receta;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ServicioEvolucion {

    List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId);

    Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, String nombreMedico , String especialidadMedico);

    Receta crearReceta(List<String> nombresMedicamentos, Long diagnosticoId, Long evolucionId, Paciente paciente, String nombreMedico, String especialidadMedico);

    byte[] generarPDFReceta(Long numeroReceta, @NotNull List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico);

    byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items);

    void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException;
}
