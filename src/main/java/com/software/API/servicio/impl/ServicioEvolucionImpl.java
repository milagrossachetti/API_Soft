package com.software.API.servicio.impl;

import com.software.API.DTOs.*;
import com.software.API.excepcion.*;
import com.software.API.modelo.*;
import com.software.API.repositorio.*;
import com.software.API.servicio.ServicioAPISalud;
import com.software.API.servicio.ServicioEmail;
import com.software.API.servicio.ServicioEvolucion;
import com.software.API.servicio.ServicioPDF;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServicioEvolucionImpl implements ServicioEvolucion {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioAPISalud servicioAPISalud;
    private final JavaMailSender mailSender;
    private final ServicioPDF servicioPDF;
    private final ServicioEmail servicioEmail;

    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucionImpl(RepositorioPaciente repositorioPaciente, ServicioAPISalud servicioAPISalud, JavaMailSender mailSender, ServicioPDF servicioPDF, ServicioEmail servicioEmail) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioAPISalud = servicioAPISalud;
        this.mailSender = mailSender;
        this.servicioPDF = servicioPDF;
        this.servicioEmail = servicioEmail;
    }

    // Obtener evoluciones de un diagnóstico específico
    @Override
    public List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        Diagnostico diagnostico = paciente.obtenerDiagnosticoPorId(diagnosticoId);
        return diagnostico.getEvoluciones();
    }

    // Crear evolución para un diagnóstico
    @Override
    public Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, String nombreMedico, String especialidadMedico) {
        if (evolucionDTO == null) {
            throw new IllegalArgumentException("Los datos de la evolución no pueden ser nulos.");
        }

        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente);
        }

        // Validar que al menos uno de los campos esté presente
        boolean tieneContenido = (evolucionDTO.getTexto() != null && !evolucionDTO.getTexto().isEmpty()) ||
                evolucionDTO.getPlantillaControl() != null ||
                evolucionDTO.getPlantillaLaboratorio() != null ||
                (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty());

        if (!tieneContenido) {
            throw new IllegalArgumentException("La evolución debe tener al menos texto, plantilla de control, plantilla de laboratorio o una receta.");
        }

        boolean tieneRecetaYLaboratorio = (evolucionDTO.getPlantillaLaboratorio() != null &&
                (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()));

        if(tieneRecetaYLaboratorio){
            throw new IllegalArgumentException("La evolucion no puede tener receta y plantilla de laboratorio al mismo tiempo.");
        }

        if (nombreMedico == null || nombreMedico.isEmpty() || especialidadMedico == null || especialidadMedico.isEmpty()) {
            throw new IllegalArgumentException("El nombre y la especialidad del médico son obligatorios.");
        }

        // Crear la evolución a través del flujo jerárquico
        Evolucion nuevaEvolucion = paciente.crearYAgregarEvolucion(
                diagnosticoId,
                evolucionDTO.getTexto(),
                nombreMedico,
                especialidadMedico,
                convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl()),
                convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio())
        );

        // Manejar las recetas si están presentes en el DTO
        if (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()) {
            for (RecetaDTO recetaDTO : evolucionDTO.getRecetas()) {
                List<String> medicamentos = recetaDTO.getMedicamentos();
                if (medicamentos == null || medicamentos.isEmpty()) {
                    throw new IllegalArgumentException("Las recetas deben contener al menos un medicamento.");
                }

                Receta receta = crearReceta(
                        medicamentos,
                        diagnosticoId,
                        nuevaEvolucion.getId(),
                        paciente,
                        nombreMedico,
                        especialidadMedico
                );

                // Generar PDF de la receta
                byte[] pdfReceta = generarPDFReceta(receta.getId(), medicamentos, paciente.getNombreCompleto(), nombreMedico, especialidadMedico, paciente.obtenerNombreObraSocial(), paciente.getNroAfiliado());
                try {
                    enviarEmailConAdjunto(paciente.getEmail(), "Receta Médica", "Adjunto encontrarás la receta médica.", pdfReceta, "receta.pdf");
                } catch (MessagingException e) {
                    logger.error("Error al enviar el correo: ", e);
                }
            }
        } else if (evolucionDTO.getPlantillaLaboratorio() != null) {
            byte[] pdfLaboratorio = generarPDFLaboratorio(paciente.getNombreCompleto(), nombreMedico, especialidadMedico, evolucionDTO.getPlantillaLaboratorio().getTiposEstudios(), evolucionDTO.getPlantillaLaboratorio().getItems(), paciente.obtenerNombreObraSocial(), paciente.getNroAfiliado());
            try {
                enviarEmailConAdjunto(paciente.getEmail(), "Pedido de Laboratorio", "Adjunto encontrarás el pedido de laboratorio.", pdfLaboratorio, "laboratorio.pdf");
            } catch (MessagingException e) {
                logger.error("Error al enviar el correo: ", e);
            }
        }

        // Guardar los cambios en el repositorio
        repositorioPaciente.guardarPaciente(paciente);

        // Registrar el evento en los logs
        logger.info("Evolución creada exitosamente para el paciente con CUIL: {}, diagnóstico ID: {}, por el médico: {} ({})",
                cuilPaciente, diagnosticoId, nombreMedico, especialidadMedico);


        // Retornar la evolución creada
        return nuevaEvolucion;
    }

    // Crear receta y asociarla a una evolución
    @Override
    public Receta crearReceta(List<String> nombresMedicamentos, Long diagnosticoId, Long evolucionId, Paciente paciente, String nombreMedico, String especialidadMedico) {
        // Validar la cantidad de medicamentos
        if (nombresMedicamentos == null || nombresMedicamentos.size() > 2) {
            throw new RecetaInvalidaException("Solo se permiten hasta 2 medicamentos por receta.");
        }

        // Validar medicamentos con la API de salud
        validarMedicamentosConApi(nombresMedicamentos, servicioAPISalud);

        // Validar obra social del paciente con la API de salud
        validarObraSocialConApi(paciente, servicioAPISalud);

        // Delegar la creación de la receta al flujo jerárquico desde paciente
        Receta receta = paciente.crearReceta(
                nombresMedicamentos, // Lista de medicamentos
                diagnosticoId,       // ID del diagnóstico
                evolucionId,         // ID de la evolución
                nombreMedico,        // Nombre del médico
                especialidadMedico   // Especialidad del médico
        );

        // Loguear la creación exitosa
        logger.info("Receta creada con medicamentos: {}", nombresMedicamentos);

        return receta;
    }

    private Paciente obtenerPacientePorCuil(Long cuil) {
        return repositorioPaciente.buscarPorCuil(cuil)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuil));
    }

    // Validar medicamentos utilizando el ServicioAPISalud
    private void validarMedicamentosConApi(List<String> nombresMedicamentos, ServicioAPISalud servicioAPISalud) {
        for (String nombre : nombresMedicamentos) {
            List<Medicamento> medicamentosEncontrados = servicioAPISalud.obtenerMedicamentosPorDescripcion(nombre);
            if (medicamentosEncontrados.isEmpty()) {
                throw new RecetaInvalidaException("El medicamento '" + nombre + "' no está disponible en la base de datos de la API de salud.");
            }
        }
    }

    private void validarObraSocialConApi(Paciente paciente, ServicioAPISalud servicioAPISalud) {
        try {
            // Obtener el código de la obra social a través del método encapsulado
            String codigoObraSocial = paciente.obtenerCodigoObraSocial();

            // Validar que la obra social exista en la API externa
            ObraSocial obraSocialApi = servicioAPISalud.obtenerObraSocialPorCodigo(codigoObraSocial);
            if (obraSocialApi == null) {
                throw new RecetaInvalidaException("La obra social con código '" + codigoObraSocial + "' no está registrada en la base de datos de la API de salud.");
            }
        } catch (IllegalArgumentException e) {
            // Excepción manejada desde obtenerCodigoObraSocial
            throw new RecetaInvalidaException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al validar la obra social en la API de salud: " + e.getMessage(), e);
        }
    }

    private PlantillaControl convertirPlantillaControlDTO(PlantillaControlDTO dto) {
        if (dto == null) return null;
        return new PlantillaControl(dto.getPeso(), dto.getAltura(), dto.getPresion(), dto.getPulso(), dto.getSaturacion(), dto.getNivelAzucar());
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) return null;
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems());
    }

    public void validarObraSocial(String codigoObraSocial) {
        try {
            ObraSocial obraSocial = servicioAPISalud.obtenerObraSocialPorCodigo(codigoObraSocial);
            if (obraSocial == null) {
                throw new IllegalArgumentException("La obra social no existe.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar la obra social: " + e.getMessage(), e);
        }
    }

    public void validarMedicamentoPorNombre(String nombreMedicamento) {
        try {
            // Llamar al método para obtener medicamentos por descripción
            List<Medicamento> medicamentos = servicioAPISalud.obtenerMedicamentosPorDescripcion(nombreMedicamento);

            // Verificar si la lista de medicamentos está vacía o no
            if (medicamentos.isEmpty()) {
                throw new IllegalArgumentException("El medicamento con el nombre '" + nombreMedicamento + "' no existe.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el medicamento por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] generarPDFReceta(Long numeroReceta, List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico, String obraSocial, String nroAfiliado){
        return servicioPDF.generarPDFReceta(numeroReceta, medicamentos, nombrePaciente, nombreMedico, especialidadMedico, obraSocial, nroAfiliado);
    }

    @Override
    public byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items, String obraSocial, String nroAfiliado) {
        return servicioPDF.generarPDFLaboratorio(nombrePaciente, nombreMedico, especialidadMedico, tiposEstudios, items,obraSocial, nroAfiliado);
    }

    @Override
    public void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException {
        servicioEmail.enviarEmailConAdjunto(destinatario, asunto, cuerpo, adjunto, nombreAdjunto);
    }
}