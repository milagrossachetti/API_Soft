package com.software.API.servicio.impl;


import com.software.API.DTOs.EvolucionDTO;
import com.software.API.excepcion.*;
import com.software.API.modelo.*;
import com.software.API.repositorio.RepositorioPaciente;
import com.software.API.servicio.ServicioDiagnostico;
import com.software.API.servicio.ServicioEvolucion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioDiagnosticoImpl implements ServicioDiagnostico {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioEvolucion servicioEvolucion;


    private static final Logger logger = LoggerFactory.getLogger(ServicioDiagnostico.class);

    public ServicioDiagnosticoImpl(RepositorioPaciente repositorioPaciente, ServicioEvolucion servicioEvolucion) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioEvolucion = servicioEvolucion;
    }

    @Override
    public List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long cuilPaciente) {
        Paciente paciente = repositorioPaciente.buscarPorCuil(cuilPaciente)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente));

        // Preguntar al paciente si tiene una historia clínica
        if (!paciente.tieneHistoriaClinica()) {
            throw new HistoriaClinicaNoEncontradaException("El paciente no tiene una historia clínica asociada.");
        }

        // Obtener la historia clínica desde el paciente
        HistoriaClinica historiaClinica = paciente.obtenerHistoriaClinica();

        return historiaClinica.getDiagnosticos();
    }


    @Override
    public Diagnostico crearDiagnostico(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, String nombreMedico, String especialidadMedico) {

        // Buscar el paciente por su CUIL
        Paciente paciente = repositorioPaciente.buscarPorCuil(cuilPaciente)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente));

        // Validar que el paciente tenga una historia clínica
        if (!paciente.tieneHistoriaClinica()) {
            throw new HistoriaClinicaNoEncontradaException("El paciente no tiene una historia clínica asociada.");
        }

        // Obtener la historia clínica del paciente
        HistoriaClinica historiaClinica = paciente.obtenerHistoriaClinica();

        // Crear el diagnóstico
        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, historiaClinica, nombreMedico, especialidadMedico);
        logger.info("ID asignado al diagnóstico antes de asignarle con currentTime: {}", nuevoDiagnostico.getId());
        // Asignar un ID único manualmente
        nuevoDiagnostico.setId(System.currentTimeMillis()); // O una lógica personalizada para generar IDs

        logger.info("ID asignado al diagnóstico despues de asignarle con currentTime: {}", nuevoDiagnostico.getId());

        // Agregar el diagnóstico a la historia clínica usando el método encapsulado
        paciente.agregarDiagnostico(nuevoDiagnostico);

        logger.info("ID asignado al diagnóstico despues de agregar el diagnostico a paciente: {}", nuevoDiagnostico.getId());
        // Guardar los cambios en el repositorio del paciente
        repositorioPaciente.guardarPaciente(paciente);

        logger.info("ID asignado al diagnóstico despues de guardar en repositorio paciente y antes de enviar el valor a crearEvolucion: {}", nuevoDiagnostico.getId());
        // Crear la evolución inicial asociada al diagnóstico
        servicioEvolucion.crearEvolucion(
                cuilPaciente,
                nuevoDiagnostico.getId(),
                evolucionDTO,
                nombreMedico,
                especialidadMedico
        );
        logger.info("ID asignado al diagnóstico al regresar de crear evolucion: {}", nuevoDiagnostico.getId());
        // Registrar la creación en los logs
        logger.info("Diagnóstico '{}' creado para el paciente con CUIL: {}", nombreDiagnostico, cuilPaciente);

        // Retornar el diagnóstico creado
        return nuevoDiagnostico;
    }




}

