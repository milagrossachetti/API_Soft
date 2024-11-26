package com.software.API.servicio;

import com.software.API.DTOs.EvolucionDTO;
import com.software.API.modelo.Evolucion;
import com.software.API.modelo.Receta;
import com.software.API.modelo.Usuario;

import java.util.List;

public interface ServicioEvolucion {

    /**
     * Obtiene las evoluciones asociadas a un diagnóstico específico de un paciente.
     *
     * @param cuilPaciente CUIL del paciente.
     * @param diagnosticoId ID del diagnóstico.
     * @return Lista de evoluciones asociadas al diagnóstico.
     */
    List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId);

    /**
     * Crea una nueva evolución asociada a un diagnóstico y la guarda en el paciente.
     *
     * @param cuilPaciente CUIL del paciente.
     * @param diagnosticoId ID del diagnóstico.
     * @param evolucionDTO Datos de la evolución a crear.
     * @param nombreMedico Médico que crea la evolución.
     * @param especialidadMedico
     * @return La evolución creada.
     */
    Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, String nombreMedico , String especialidadMedico);

    /**
     * Crea una receta asociada a una evolución específica.
     *
     * @param nombresMedicamentos Lista de nombres de medicamentos a incluir en la receta.
     * @param evolucion Evolución a la que se asocia la receta.
     * @param nombreMedico Médico que crea la receta.
     * @param especialidadMedico
     * @return La receta creada.
     */
    Receta crearReceta(List<String> nombresMedicamentos, Evolucion evolucion, String nombreMedico, String especialidadMedico) ;

    /**
     * Genera un archivo PDF para una receta específica.
     *
     * @param receta Receta para la cual se generará el PDF.
     */
    void generarPdfReceta(Receta receta);

    /**
     * Envía por correo electrónico el PDF de una evolución específica.
     *
     * @param cuilPaciente CUIL del paciente.
     * @param diagnosticoId ID del diagnóstico.
     * @param evolucionId ID de la evolución.
     * @param email Dirección de correo electrónico a la cual enviar el PDF.
     */
    void enviarPdfEvolucion(Long cuilPaciente, Long diagnosticoId, Long evolucionId, String email);
}
