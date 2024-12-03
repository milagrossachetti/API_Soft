package com.software.API.servicio.impl;

import com.software.API.modelo.Paciente;
import com.software.API.modelo.HistoriaClinica;
import com.software.API.repositorio.RepositorioPaciente;
import com.software.API.servicio.ServicioPaciente;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioPacienteImpl implements ServicioPaciente {

    private final RepositorioPaciente repositorioPaciente;

    // Constructor con inyección de dependencias
    public ServicioPacienteImpl(RepositorioPaciente repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    // Obtener un paciente por CUIL
    @Override
    public Paciente obtenerPacientePorCuil(Long cuil) {
        return repositorioPaciente.buscarPorCuil(cuil)
                .orElseThrow(() -> new IllegalArgumentException("Paciente con CUIL " + cuil + " no encontrado."));
    }

    // Actualizar la historia clínica completa de un paciente
    @Override
    public void actualizarHistoriaClinica(Long cuil, HistoriaClinica historiaActualizada) {
        Paciente paciente = obtenerPacientePorCuil(cuil);
        paciente.setHistoriaClinica(historiaActualizada);

        // Persistir el cambio
        repositorioPaciente.guardarPaciente(paciente);
    }

    // Obtener la historia clínica de un paciente
    @Override
    public HistoriaClinica obtenerHistoriaClinica(Long cuil) {
        Paciente paciente = obtenerPacientePorCuil(cuil);
        return Optional.ofNullable(paciente.getHistoriaClinica())
                .orElseThrow(() -> new IllegalStateException("El paciente con CUIL " + cuil + " no tiene una historia clínica asociada."));
    }
}

