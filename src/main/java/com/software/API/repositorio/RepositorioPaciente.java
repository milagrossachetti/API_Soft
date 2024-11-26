package com.software.API.repositorio;


import com.software.API.modelo.Paciente;


import java.util.Optional;

public interface RepositorioPaciente {
    Optional<Paciente> buscarPorCuil(Long cuil); // Busca un paciente por CUIL.
    void guardarPaciente(Paciente paciente);    // Guarda o actualiza un paciente.

}
