package com.software.API.repositorio;

import com.software.API.modelo.Paciente;

import java.util.Optional;

public interface Repositorio {
    Paciente buscarPacientePorCuil(Long cuil);
    void guardarPaciente(Paciente paciente);
}
