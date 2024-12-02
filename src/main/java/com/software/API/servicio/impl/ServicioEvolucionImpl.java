package com.software.API.servicio.impl;

import com.software.API.DTOs.*;
import com.software.API.excepcion.*;
import com.software.API.modelo.*;
import com.software.API.repositorio.*;
import com.software.API.servicio.ServicioAPISalud;
import com.software.API.servicio.ServicioEmail;
import com.software.API.servicio.ServicioEvolucion;
import jakarta.mail.MessagingException;
import com.software.API.servicio.ServicioPDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class ServicioEvolucionImpl implements ServicioEvolucion {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioAPISalud servicioAPISalud;
    private final ServicioPDF servicioPDF;
    private final ServicioEmail servicioEmail;



    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucionImpl(
            RepositorioPaciente repositorioPaciente,
            ServicioAPISalud servicioAPISalud,
            ServicioPDF servicioPDF,
            ServicioEmail servicioEmail) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioAPISalud = servicioAPISalud;
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

        // Validar que al menos uno de los campos esté presente
        boolean tieneContenido = (evolucionDTO.getTexto() != null && !evolucionDTO.getTexto().isEmpty()) ||
                evolucionDTO.getPlantillaControl() != null ||
                evolucionDTO.getPlantillaLaboratorio() != null ||
                (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty());

        if (!tieneContenido) {
            throw new IllegalArgumentException("La evolución debe tener al menos texto, plantilla de control, plantilla de laboratorio o una receta.");
        }

        // Validar que no tenga receta y plantilla de laboratorio juntas
        if (evolucionDTO.getPlantillaLaboratorio() != null &&
                evolucionDTO.getRecetas() != null &&
                !evolucionDTO.getRecetas().isEmpty()) {
            throw new IllegalArgumentException("La evolución no puede contener receta y plantilla de laboratorio al mismo tiempo.");
        }

        //Validar medico
        if (nombreMedico == null || nombreMedico.isEmpty() || especialidadMedico == null || especialidadMedico.isEmpty()) {
            throw new IllegalArgumentException("El nombre y la especialidad del médico son obligatorios.");
        }

        // Obtener el paciente de manera encapsulada
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente);
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

                // Invocar el método crearReceta
                crearReceta(
                        medicamentos,
                        diagnosticoId,
                        nuevaEvolucion.getId(),
                        paciente,
                        nombreMedico,
                        especialidadMedico
                );
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


    @Override
    public byte[] generarPDFReceta(Long numeroReceta, List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico) {
        return servicioPDF.generarPDFReceta(numeroReceta, medicamentos, nombrePaciente, nombreMedico, especialidadMedico);
    }

    @Override
    public byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items) {
        return servicioPDF.generarPDFLaboratorio(nombrePaciente, nombreMedico, especialidadMedico, tiposEstudios, items);
    }

    @Override
    public void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException {
        servicioEmail.enviarEmailConAdjunto(destinatario, asunto, cuerpo, adjunto, nombreAdjunto);
    }


}
