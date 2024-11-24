package com.software.API.repositorio;

import com.software.API.modelo.Diagnostico;
import com.software.API.modelo.HistoriaClinica;
import com.software.API.modelo.Paciente;
import com.software.API.modelo.TipoDiagnostico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
//CONSULTAR DIAGNOSTICO. ESTAMOS HACIENDO BIEN?????
public class RepositorioImpl implements Repositorio{
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Diagnostico> diagnosticos = new ArrayList<>();
    public void iniciar() {
        crearPacientes();
    }
    private void crearPacientes(){
        pacientes.add(new Paciente(
                1L,
                12456789L,
                "Juan Perez",
                new Date(),
                "381547896",
                "juan@gmail.com",
                "Calle Falsa 123",
                "San Miguel",
                "Buenos Aires",
                "Argentina",
                "AF12345",
                1L
        ));

        pacientes.add(new Paciente(
                2L, 22333444L,
                "Maria Lopez", new Date(), "385123456", "maria@gmail.com", "Av. Siempre Viva 456",
                "San Salvador", "Jujuy", "Argentina", "AF54321", 2L
        ));
        pacientes.add(new Paciente(
                3L, 33445566L, "Carlos Gomez", new Date(), "381678945", "carlos@gmail.com",
                "Av. Mitre 789", "Córdoba", "Córdoba", "Argentina", "AF67890", 3L
        ));

        pacientes.add(new Paciente(
                4L, 44556677L, "Lucia Fernandez", new Date(), "381789123", "lucia@gmail.com",
                "Calle Libertad 987", "Rosario", "Santa Fe", "Argentina", "AF98765", 4L
        ));

        pacientes.add(new Paciente(
                5L, 55667788L, "Sofia Martinez", new Date(), "385123789", "sofia@gmail.com",
                "Boulevard Oroño 123", "Santa Fe", "Santa Fe", "Argentina", "AF54367", 5L
        ));

        pacientes.add(new Paciente(
                6L, 66778899L, "Miguel Alvarez", new Date(), "380123456", "miguel@gmail.com",
                "Calle Mendoza 456", "Tucumán", "Tucumán", "Argentina", "AF76543", 6L
        ));

        pacientes.add(new Paciente(
                7L, 77889900L, "Florencia Diaz", new Date(), "381456789", "florencia@gmail.com",
                "Av. Roca 789", "Salta", "Salta", "Argentina", "AF34567", 7L
        ));
    }

    @Override
    public Paciente buscarPacientePorCuil(Long cuil) {
        return pacientes.get(pacientes.indexOf(cuil));
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        //preguntar
    }


}
