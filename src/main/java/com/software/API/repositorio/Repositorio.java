package com.software.API.repositorio;

import com.software.API.modelo.Paciente;
import com.software.API.modelo.TipoDiagnostico;

import java.util.Optional;

public interface Repositorio {
    Paciente buscarPacientePorCuil(Long cuil);
    void guardarPaciente(Paciente paciente);
}
