package com.software.API;

import com.software.API.DTOs.EvolucionDTO;
import com.software.API.DTOs.PlantillaControlDTO;
import com.software.API.DTOs.RecetaDTO;
import com.software.API.modelo.*;
import com.software.API.repositorio.RepositorioPaciente;
import com.software.API.servicio.ServicioEvolucion;
import com.software.API.servicio.ServicioPaciente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ServicioEvolucionTest {

    private ServicioEvolucion servicioEvolucion;

    @Mock
    private RepositorioPaciente repositorioPaciente;

    @Mock
    private ServicioPaciente servicioPaciente;


    @BeforeEach
    public void setup(){
        this.repositorioPaciente = mock(RepositorioPaciente.class);
        this.servicioEvolucion = mock(ServicioEvolucion.class);
        this.servicioPaciente = mock(ServicioPaciente.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void obtenerEvolucionesDeUnDiagnosticoDevuelveEvoluciones(){
        //Arrange -> preparar el escenario
        HistoriaClinica historia = new HistoriaClinica();
        Paciente paciente = new Paciente(
                123454488L,
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
        paciente.setHistoriaClinica(historia);
        Diagnostico diagnostico = new Diagnostico("Sinusitis frontal aguda", "Dr. Pedro Martínez", "Pediatría");
        historia.setDiagnosticos(List.of(diagnostico));
        Evolucion evolucion = new Evolucion(
                "Paciente con congestión severa y dolor frontal.",
                LocalDateTime.now(),
                "Dr. Pedro Martínez", // Mismo médico del diagnóstico
                "Pediatría", // Especialidad
                new PlantillaControl(68.0, 1.75, "130/85", 85, 96, null), // Plantilla de control
                null, // Sin plantilla de laboratorio
                new ArrayList<>() // Sin recetas al inicio
        );
        diagnostico.agregarEvolucion(evolucion);
        List<Evolucion> evoluciones = List.of(evolucion);

        when(this.servicioEvolucion.obtenerEvolucionesDelDiagnostico(paciente.getCuil(), diagnostico.getId())).thenReturn(evoluciones);

        //Act -> ejecuto el escenario
        List<Evolucion> respuesta;
        respuesta = servicioEvolucion.obtenerEvolucionesDelDiagnostico(paciente.getCuil(),diagnostico.getId());

        //Assert -> compruebo que el escenario pase
        assertThat(respuesta).isEqualTo(evoluciones);
    }

    @Test
    public void crearEvolucionConTexto (){
        //Arrange
        HistoriaClinica historia = new HistoriaClinica();
        Paciente paciente = new Paciente(
                123454488L,
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
        paciente.setHistoriaClinica(historia);
        Diagnostico diagnostico = new Diagnostico("Sinusitis frontal aguda", "Dr. Pedro Martínez", "Pediatría");
        historia.setDiagnosticos(List.of(diagnostico));

        EvolucionDTO nuevaEvolucionDTO = new EvolucionDTO(
                "Paciente con congestión severa y dolor frontal.",
                null,
                null,
                null
        );

        Evolucion nuevaEvolucion = new Evolucion(
                nuevaEvolucionDTO.getTexto(),
                LocalDateTime.now(),
                "Dr. Pedro Martínez",
                "Pediatría",
                null,
                null,
                null
        );

        diagnostico.agregarEvolucion(nuevaEvolucion);

        when(servicioEvolucion.crearEvolucion(
                paciente.getCuil(),
                diagnostico.getId(),
                nuevaEvolucionDTO,
                "Dr. Pedro Martínez",
                "Pediatría"
        )).thenReturn(nuevaEvolucion);


        //Act
        Evolucion respuesta = servicioEvolucion.crearEvolucion(
                paciente.getCuil(),
                diagnostico.getId(),
                nuevaEvolucionDTO,
                "Dr. Pedro Martínez",
                "Pediatría"
        );

        //Assert
        assertThat(respuesta).isNotNull();
        assertThat(respuesta.getTexto()).isEqualTo(nuevaEvolucionDTO.getTexto());
        assertThat(respuesta.getNombreMedico()).isEqualTo("Dr. Pedro Martínez");
        assertThat(respuesta.getEspecialidadMedico()).isEqualTo("Pediatría");
        assertThat(diagnostico.obtenerEvoluciones().contains(nuevaEvolucion));

        verify(servicioEvolucion, times(1)).crearEvolucion(
                paciente.getCuil(),
                diagnostico.getId(),
                nuevaEvolucionDTO,
                "Dr. Pedro Martínez",
                "Pediatría"
        );
    }

    @Test
    public void crearEvolucionIncompleta() {
        //Arrange -> preparar el escenario
        HistoriaClinica historia = new HistoriaClinica();
        Paciente paciente = new Paciente(
                123454488L,
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
                new ObraSocial("128409", "OBRA SOCIAL DEL SINDICATO OBREROS Y EMPLEADOS DE EMPRESAS DE LIMPIEZA,SERVICIOS Y AFINES DE CORDOBA", "OSSOELSAC")
        );
        paciente.setHistoriaClinica(historia);
        Diagnostico diagnostico = new Diagnostico("Sinusitis frontal aguda", "Dr. Pedro Martínez", "Pediatría");
        historia.setDiagnosticos(List.of(diagnostico));

        //RECORDAR CONDICION DE PLANTILLA LABORATORIO O RECETA, NO AMBAS!
        EvolucionDTO nuevaEvolucionDTO = new EvolucionDTO(
                null,
                null,
                null,
                null

        );

        when(servicioEvolucion.crearEvolucion(
                paciente.getCuil(),
                diagnostico.getId(),
                nuevaEvolucionDTO,
                "Dr. Pedro Martínez",
                "Pediatría"
        )).thenThrow(new IllegalArgumentException("La evolución no puede ser nula."));

        //Act y assert
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> servicioEvolucion.crearEvolucion(
                        paciente.getCuil(),
                        diagnostico.getId(),
                        nuevaEvolucionDTO,
                        "Dr. Pedro Martínez",
                        "Pediatría"
                )
        );

        org.assertj.core.api.Assertions.assertThat(exception.getMessage()).isEqualTo("La evolución no puede ser nula.");
    }

    @Test
    public void crearEvolucionErrorEnPlantillaDeControl() {
        //Arrange -> preparar el escenario
        HistoriaClinica historia = new HistoriaClinica();
        Paciente paciente = new Paciente(
                123454488L,
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
                new ObraSocial("128409", "OBRA SOCIAL DEL SINDICATO OBREROS Y EMPLEADOS DE EMPRESAS DE LIMPIEZA,SERVICIOS Y AFINES DE CORDOBA", "OSSOELSAC")
        );
        paciente.setHistoriaClinica(historia);
        Diagnostico diagnostico = new Diagnostico("Sinusitis frontal aguda", "Dr. Pedro Martínez", "Pediatría");
        historia.setDiagnosticos(List.of(diagnostico));

        EvolucionDTO nuevaEvolucionDTO = new EvolucionDTO(
                null,
                new PlantillaControlDTO(
                        -72.3,
                        -10.78,
                        "500-8000",
                        -750,
                        -97,
                        -950.0
                ),
                null,
                null
        );

        when(servicioEvolucion.crearEvolucion(
                paciente.getCuil(),
                diagnostico.getId(),
                nuevaEvolucionDTO,
                "Dr. Pedro Martínez",
                "Pediatría"
        )).thenThrow(new IllegalArgumentException("Los datos de la plantilla de control son inválidos."));

        //Act y assert
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> servicioEvolucion.crearEvolucion(
                        paciente.getCuil(),
                        diagnostico.getId(),
                        nuevaEvolucionDTO,
                        "Dr. Pedro Martínez",
                        "Pediatría"
                )
        );

        org.assertj.core.api.Assertions.assertThat(exception.getMessage()).isEqualTo("Los datos de la plantilla de control son inválidos.");
    }

}