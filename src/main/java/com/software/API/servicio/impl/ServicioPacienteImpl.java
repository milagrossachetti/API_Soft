package com.software.API.servicio.impl;

import com.software.API.modelo.Paciente;
import com.software.API.modelo.HistoriaClinica;
import com.software.API.repositorio.RepositorioPaciente;
import com.software.API.repositorio.impl.RepositorioPacienteMemoria;
import com.software.API.servicio.ServicioPaciente;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioPacienteImpl implements ServicioPaciente {

    private final RepositorioPaciente repositorioPaciente;
    private final RepositorioPacienteMemoria repositorioPacienteMemoria;

    // Constructor con inyección de dependencias
    public ServicioPacienteImpl(RepositorioPaciente repositorioPaciente, RepositorioPacienteMemoria repositorioPacienteMemoria) {
        this.repositorioPaciente = repositorioPaciente;
        this.repositorioPacienteMemoria = repositorioPacienteMemoria;
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

    @Override
    public List<Paciente> buscarPacientesPorCuilParcial(Long cuil) {
        List<Paciente> pacientes = repositorioPacienteMemoria.getPacientes();
        return pacientes.stream()
                .filter(paciente -> String.valueOf(paciente.getCuil()).contains(String.valueOf(cuil)))
                .collect(Collectors.toList());
    }
}

