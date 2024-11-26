package com.software.API.repositorio;


import com.software.API.modelo.Paciente;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RepositorioPaciente {
    Optional<Paciente> buscarPorCuil(Long cuil); // Busca un paciente por CUIL.
    void guardarPaciente(Paciente paciente);    // Guarda o actualiza un paciente.

}
