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

        // Crear el diagnóstico
        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, nombreMedico, especialidadMedico);

        //ERROR POR PERDIDA DE ID-NO SE ALMACENA
        // Asignar un ID único manualmente
        nuevoDiagnostico.setId(System.currentTimeMillis()); // O una lógica personalizada para generar IDs

        // Agregar el diagnóstico a la historia clínica usando el método encapsulado
        paciente.agregarDiagnostico(nuevoDiagnostico);

        // Guardar los cambios en el repositorio del paciente
        repositorioPaciente.guardarPaciente(paciente);

        nuevoDiagnostico.setId(System.currentTimeMillis());
        logger.info("ID del diagnóstico después de asignar nuevo ID: {}", nuevoDiagnostico.getId());
        // Crear la evolución inicial asociada al diagnóstico
        servicioEvolucion.crearEvolucion(
                cuilPaciente,
                nuevoDiagnostico.getId(),
                evolucionDTO,
                nombreMedico,
                especialidadMedico
        );

        // Registrar la creación en los logs
        logger.info("Diagnóstico '{}' creado para el paciente con CUIL: {}", nombreDiagnostico, cuilPaciente);

        // Retornar el diagnóstico creado
        return nuevoDiagnostico;
    }




}

