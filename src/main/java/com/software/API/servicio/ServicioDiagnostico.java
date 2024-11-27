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

    Diagnostico crearDiagnostico(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, String nombreMedico, String especialidadMedico);
}