package com.software.API.servicio;


import com.software.API.DTOs.EvolucionDTO;
import com.software.API.modelo.Diagnostico;
import com.software.API.modelo.Usuario;

import java.util.List;

public interface ServicioDiagnostico {

    /**
     * Obtiene los diagnósticos del historial clínico de un paciente por su CUIL.
     *
     * @param cuilPaciente CUIL del paciente.
     * @return Lista de diagnósticos asociados al historial clínico del paciente.
     */
    List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long cuilPaciente);

    /**
     * Crea un nuevo diagnóstico para un paciente específico y lo asocia a su historial clínico.
     * Además, crea una evolución inicial asociada al diagnóstico.
     *
     * @param cuilPaciente CUIL del paciente.
     * @param nombreDiagnostico Nombre del diagnóstico.
     * @param evolucionDTO Datos de la evolución inicial asociados al diagnóstico.
     * @param nombreMedico Usuario médico que crea el diagnóstico.
     * @param especialidadMedico Usuario médico que crea el diagnóstico.
     * @return El diagnóstico creado.
     */
    Diagnostico crearDiagnostico(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, String nombreMedico, String especialidadMedico);
}

