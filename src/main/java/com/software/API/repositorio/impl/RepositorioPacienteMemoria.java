package com.software.API.repositorio.impl;

import com.software.API.modelo.*;
import com.software.API.repositorio.RepositorioPaciente;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class RepositorioPacienteMemoria implements RepositorioPaciente {

    private final List<Paciente> pacientes = new ArrayList<>();

    // Constructor sin dependencia de RepositorioUsuario
    public RepositorioPacienteMemoria() {
        inicializarDatos();
    }

    private void inicializarDatos() {
        // Crear Historia Clínica y Diagnósticos
        HistoriaClinica historia1 = new HistoriaClinica();
        historia1.setId(1L);

        // Crear Diagnóstico y Evoluciones
        Diagnostico diagnostico1 = new Diagnostico("Fiebre tifoidea", "Dr. Juan Pérez", "Cardiología");
        diagnostico1.setId(1L); // Asignar ID único al diagnóstico

        Evolucion evolucion1 = new Evolucion(
                "Paciente presenta fiebre persistente.",
                LocalDateTime.now(),
                "Dr. Juan Pérez", // Nombre del médico
                "Cardiología", // Especialidad
                new PlantillaControl(65.5, 1.70, "120/80", 80, 98, null),
                null, // Sin plantilla de laboratorio
                new ArrayList<>() // Sin recetas al inicio
        );
        diagnostico1.agregarEvolucion(evolucion1);

        Evolucion evolucion2 = new Evolucion(
                "Fiebre disminuye, pero continúa malestar general.",
                LocalDateTime.now(),
                "Dr. Pedro Martínez", // Otro médico
                "Pediatría", // Especialidad
                null, // Sin plantilla de control
                null, // Sin plantilla de laboratorio
                Arrays.asList(new Receta(
                        LocalDateTime.now(),
                        Arrays.asList(
                                new MedicamentoRecetado("Paracetamol"),
                                new MedicamentoRecetado("Ibuprofeno")
                        ),
                        "Dr. Pedro Martínez", // Médico
                        "Pediatría" // Especialidad
                ))
        );
        diagnostico1.agregarEvolucion(evolucion2);

        historia1.getDiagnosticos().add(diagnostico1);

        Diagnostico diagnostico2 = new Diagnostico("Sinusitis frontal aguda", "Dr. Pedro Martínez", "Pediatría");
        diagnostico2.setId(2L); // Asignar ID único al diagnóstico

        Evolucion evolucion3 = new Evolucion(
                "Paciente con congestión severa y dolor frontal.",
                LocalDateTime.now(),
                "Dr. Pedro Martínez", // Mismo médico del diagnóstico
                "Pediatría", // Especialidad
                new PlantillaControl(68.0, 1.75, "130/85", 85, 96, null), // Plantilla de control
                null, // Sin plantilla de laboratorio
                new ArrayList<>() // Sin recetas al inicio
        );

        Receta receta = new Receta(
                LocalDateTime.now(),
                Arrays.asList(
                        new MedicamentoRecetado("Amoxicilina"),
                        new MedicamentoRecetado("Aspirina")
                ),
                "Dr. Pedro Martínez", // Médico
                "Pediatría" // Especialidad
        );
        evolucion3.getRecetas().add(receta);
        diagnostico2.agregarEvolucion(evolucion3);

        historia1.getDiagnosticos().add(diagnostico2);

        // Crear Paciente con la Historia Clínica
        Paciente paciente1 = new Paciente(
                20304050607L,
                12345678L,
                "Brenda Marinelli",
                new Date(93, 4, 15),
                "3816404000",
                "marinellibrendaluciana@gmail.com",
                "Esteban Echeverria 2200",
                "San Miguel de Tucuman",
                "Tucuman",
                "Argentina",
                "AF123456",
                new ObraSocial("128409", "OBRA SOCIAL DEL SINDICATO OBREROS Y EMPLEADOS DE EMPRESAS DE LIMPIEZA,SERVICIOS Y AFINES DE CORDOBA", "OSSOELSAC") // Historia clínica ID
        );
        paciente1.setHistoriaClinica(historia1);
        pacientes.add(paciente1);

        //----otro paciente

        HistoriaClinica historia2 = new HistoriaClinica();
        historia2.setId(2L);

        Diagnostico diagnostico3 = new Diagnostico("Faringitis", "Dr. Juan Pérez", "Otorrinolaringologo");
        diagnostico3.setId(3L); // Asignar ID único al diagnóstico

        Evolucion evolucion4 = new Evolucion(
                "Fiebre, dolor de garganta y dificultad para tragar",
                LocalDateTime.now(),
                "Dr. Juan Pérez",
                "Otorrinolaringología", // Especialidad
                null, // Sin plantilla de control
                null, // Sin plantilla de laboratorio
                Arrays.asList(new Receta(
                        LocalDateTime.now(),
                        Arrays.asList(
                                new MedicamentoRecetado("Paracetamol"),
                                new MedicamentoRecetado("Ibuprofeno")
                        ),
                        "Dr. Juan Pérez", // Médico
                        "Otorrinolaringología" // Especialidad
                ))
        );
        diagnostico3.agregarEvolucion(evolucion4);

        historia2.getDiagnosticos().add(diagnostico3);

        // Crear Paciente con la Historia Clínica
        Paciente paciente2 = new Paciente(
                20394538717L,
                44965125L,
                "Milagros Sachetti",
                new Date(02, 8, 28),
                "3865348167",
                "milagrossachetti@gmail.com",
                "Balcarce 400",
                "San Miguel de Tucuman",
                "Tucuman",
                "Argentina",
                "AG568142",
                new ObraSocial("111506","OBRA SOCIAL ASOCIACION PROFESIONAL DE CAPITANES Y BAQUEANOS FLUVIALES Y PERSONAL CON  CONOCIMIENTO DE ZONA DE LA MARINA MERCANTE","OSCAPBAQFLU") // Historia clínica ID
        );
        paciente2.setHistoriaClinica(historia2);
        pacientes.add(paciente2);

        //----otro paciente

        HistoriaClinica historia3 = new HistoriaClinica();
        historia3.setId(3L);

        // Crear Diagnóstico y Evoluciones para el segundo paciente
        Diagnostico diagnostico4 = new Diagnostico("Gripe común", "Dr. Ana López", "Medicina General");
        diagnostico3.setId(4L); // Asignar ID único al diagnóstico

        Evolucion evolucion5 = new Evolucion(
                "Paciente presenta síntomas de gripe.",
                LocalDateTime.now(),
                "Dr. Ana López", // Nombre del médico
                "Medicina General", // Especialidad
                new PlantillaControl(70.0, 1.80, "110/70", 75, 97, null),
                null, // Sin plantilla de laboratorio
                new ArrayList<>() // Sin recetas al inicio
        );
        diagnostico3.agregarEvolucion(evolucion4);

        historia3.getDiagnosticos().add(diagnostico3);

        Diagnostico diagnostico6 = new Diagnostico("Bronquitis aguda", "Dr. Carlos Gómez", "Neumología");
        diagnostico4.setId(4L); // Asignar ID único al diagnóstico

        Evolucion evolucion8 = new Evolucion(
                "Paciente con tos persistente y dificultad para respirar.",
                LocalDateTime.now(),
                "Dr. Carlos Gómez", // Mismo médico del diagnóstico
                "Neumología", // Especialidad
                new PlantillaControl(72.0, 1.75, "120/80", 80, 95, null), // Plantilla de control
                null, // Sin plantilla de laboratorio
                new ArrayList<>() // Sin recetas al inicio
        );

        Receta receta2 = new Receta(
                LocalDateTime.now(),
                Arrays.asList(
                        new MedicamentoRecetado("Broncodilatador"),
                        new MedicamentoRecetado("Antibiótico")
                ),
                "Dr. Carlos Gómez", // Médico
                "Neumología" // Especialidad
        );
        evolucion5.getRecetas().add(receta2);
        diagnostico4.agregarEvolucion(evolucion5);

        historia3.getDiagnosticos().add(diagnostico4);

        // Crear el segundo paciente con la Historia Clínica
        Paciente paciente3 = new Paciente(
                27450611323L,
                450611323L,
                "Ariadna Cisterna Diaco",
                new Date(03, 06, 10),
                "3816532320",
                "cisterna2728@gmail.com",
                "Centenario 353",
                "Tafi Viejo",
                "Tucuman",
                "Argentina",
                "BF654321",
                new ObraSocial("", "OBRA SOCIAL FEDERAL DE LA FEDERACION NACIONAL DE TRABAJADORES DE OBRAS SANITARIAS", "OSFFENTOS") // Historia clínica ID
        );
        paciente3.setHistoriaClinica(historia3);
        pacientes.add(paciente3);
    }

    @Override
    public Optional<Paciente> buscarPorCuil(Long cuil) {
        return pacientes.stream()
                .filter(paciente -> paciente.getCuil().equals(cuil))
                .findFirst();
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        buscarPorCuil(paciente.getCuil())
                .ifPresent(existente -> {
                    HistoriaClinica nuevaHistoriaClinica = paciente.getHistoriaClinica();
                    if (nuevaHistoriaClinica != null) {
                        // Obtener diagnósticos actuales
                        List<Diagnostico> diagnosticosExistentes = existente.getHistoriaClinica().getDiagnosticos();

                        // Fusionar diagnósticos nuevos
                        nuevaHistoriaClinica.getDiagnosticos().forEach(diagnosticoNuevo -> {
                            boolean yaExiste = diagnosticosExistentes.stream()
                                    .anyMatch(d -> d.getId().equals(diagnosticoNuevo.getId()));
                            if (!yaExiste) {
                                diagnosticosExistentes.add(diagnosticoNuevo);
                            }
                        });

                        // Actualizar la historia clínica con los diagnósticos fusionados
                        existente.getHistoriaClinica().setDiagnosticos(diagnosticosExistentes);
                    }
                });
    }
    public List<Paciente> getPacientes() {
        return pacientes;
    }
}