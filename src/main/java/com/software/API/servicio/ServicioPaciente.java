package com.software.API.servicio;

import com.software.API.modelo.Paciente;
import com.software.API.modelo.HistoriaClinica;

import java.util.List;

public interface ServicioPaciente {

    /**
     * Obtiene un paciente por su CUIL.
     *
     * @param cuil CUIL del paciente a buscar.
     * @return El paciente asociado al CUIL proporcionado.
     * @throws IllegalArgumentException Si no se encuentra un paciente con el CUIL proporcionado.
     */
    Paciente obtenerPacientePorCuil(Long cuil);

    /**
     * Actualiza la historia clínica de un paciente.
     *
     * @param cuil CUIL del paciente cuya historia clínica se desea actualizar.
     * @param historiaActualizada La nueva historia clínica para el paciente.
     */
    void actualizarHistoriaClinica(Long cuil, HistoriaClinica historiaActualizada);

    /**
     * Obtiene la historia clínica de un paciente.
     *
     * @param cuil CUIL del paciente cuya historia clínica se desea obtener.
     * @return La historia clínica asociada al paciente.
     * @throws IllegalStateException Si el paciente no tiene una historia clínica asociada.
     */
    HistoriaClinica obtenerHistoriaClinica(Long cuil);

    List<Paciente> buscarPacientesPorCuilParcial(Long cuil);
}
