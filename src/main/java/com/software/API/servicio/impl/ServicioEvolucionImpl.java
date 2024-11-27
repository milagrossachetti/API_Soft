package com.software.API.servicio.impl;

import com.software.API.DTOs.*;
import com.software.API.excepcion.*;
import com.software.API.modelo.*;
import com.software.API.repositorio.*;
import com.software.API.repositorio.RepositorioAPISalud;
import com.software.API.servicio.ServicioAPISalud;
import com.software.API.servicio.ServicioEvolucion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicioEvolucionImpl implements ServicioEvolucion {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioAPISalud servicioAPISalud;
    private final RepositorioAPISalud repositorioAPISalud;


    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucionImpl(RepositorioPaciente repositorioPaciente, ServicioAPISalud servicioAPISalud,RepositorioAPISalud repositorioAPISalud) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioAPISalud = servicioAPISalud;
        this.repositorioAPISalud = repositorioAPISalud;
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
        validarMedicamentos(nombresMedicamentos);

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
    private void validarMedicamentos(List<String> nombresMedicamentos) {
        List<Medicamento> apiResult = repositorioAPISalud.obtenerMedicamentos();

        // Lista de nombres válidos desde la API
        List<String> nombresValidos = apiResult.stream()
                .map(Medicamento::getNombre)
                .collect(Collectors.toList());

        // Validar que todos los medicamentos ingresados estén en la lista válida
        for (String nombre : nombresMedicamentos) {
            if (!nombresValidos.contains(nombre)) {
                throw new MedicamentoInvalidoException("El medicamento " + nombre + " no es válido.");
            }
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

}
