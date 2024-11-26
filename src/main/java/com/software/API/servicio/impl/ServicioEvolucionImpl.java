package com.software.API.servicio.impl;

import com.software.API.DTOs.*;
import com.software.API.excepcion.*;
import com.software.API.modelo.*;
import com.software.API.repositorio.*;
import com.software.API.servicio.ServicioAPISalud;
import com.software.API.servicio.ServicioEvolucion;
import com.software.API.servicio.ServicioEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicioEvolucionImpl implements ServicioEvolucion {

    private final ServicioEmail servicioEmail;
    private final RepositorioPaciente repositorioPaciente;
    private final ServicioAPISalud servicioAPISalud;


    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucionImpl(RepositorioPaciente repositorioPaciente, ServicioEmail servicioEmail, ServicioAPISalud servicioAPISalud) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioEmail = servicioEmail;
        this.servicioAPISalud = servicioAPISalud;
    }

    // Obtener evoluciones de un diagnóstico específico
    @Override
    public List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        Diagnostico diagnostico = obtenerDiagnosticoDePaciente(paciente, diagnosticoId);
        return diagnostico.getEvoluciones();
    }

    // Crear evolución para un diagnóstico
    @Override
    public Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, Usuario medico) {
        // Validar que el médico esté autenticado
        validarMedico(medico);

        // Obtener el paciente y su diagnóstico de manera encapsulada
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        Diagnostico diagnostico = obtenerDiagnosticoDePaciente(paciente, diagnosticoId);

        // Crear y agregar la evolución al diagnóstico
        Evolucion nuevaEvolucion = diagnostico.crearYAgregarEvolucion(
                evolucionDTO.getTexto(),
                medico,
                convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl()),
                convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio())
        );

        // Manejar recetas si están presentes en el DTO
        if (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()) {
            for (RecetaDTO recetaDTO : evolucionDTO.getRecetas()) {
                List<String> medicamentos = recetaDTO.getMedicamentos(); // Respeta el encapsulamiento
                crearReceta(medicamentos, nuevaEvolucion, medico);
            }
        }

        // Guardar los cambios en el repositorio
        repositorioPaciente.guardarPaciente(paciente);

        // Registrar el evento en los logs
        logger.info("Evolución creada exitosamente para el diagnóstico ID: {}", diagnosticoId);

        // Retornar la evolución creada
        return nuevaEvolucion;
    }


    // Crear receta y asociarla a una evolución
    @Override
    public Receta crearReceta(List<String> nombresMedicamentos, Evolucion evolucion, Usuario medico) {
        if (nombresMedicamentos.size() > 2) {
            throw new RecetaInvalidaException("Solo se permiten hasta 2 medicamentos por receta.");
        }

        // Validar medicamentos con la API de salud
        validarMedicamentos(nombresMedicamentos);

        // Crear los medicamentos recetados
        List<MedicamentoRecetado> medicamentos = nombresMedicamentos.stream()
                .map(this::crearMedicamentoRecetado)
                .collect(Collectors.toList());

        Receta receta = new Receta(LocalDateTime.now(), medico, medicamentos, evolucion, null);
        evolucion.agregarReceta(receta); // Encapsulamiento respetado
        logger.info("Receta creada con medicamentos: {}", nombresMedicamentos);
        return receta;
    }


    // Generar PDF de receta
    @Override
    public void generarPdfReceta(Receta receta) {
        if (receta == null) {
            throw new RuntimeException("Receta no puede ser nula para generar el PDF.");
        }

        if (receta.getMedicamentos() == null || receta.getMedicamentos().isEmpty()) {
            throw new RuntimeException("La receta no contiene medicamentos.");
        }

        // Generar un nombre único para el PDF
        String pdfPath = "ruta/ficticia/receta_" + UUID.randomUUID() + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(new File(pdfPath))) {
            fos.write(("PDF generado para receta con medicamentos: " + receta.getMedicamentos()).getBytes());

            // Encapsular la asignación de la ruta en el método de Receta
            receta.asignarRutaPdf(pdfPath);

            logger.info("PDF generado en: {}", pdfPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }

    // Enviar PDF asociado a una evolución
    @Override
    public void enviarPdfEvolucion(Long cuilPaciente, Long diagnosticoId, Long evolucionId, String email) {
        Evolucion evolucion = obtenerEvolucionPorId(cuilPaciente, diagnosticoId, evolucionId);

        if (evolucion.getRutaPdf() == null) {
            throw new RuntimeException("No hay un PDF generado para esta evolución.");
        }

        String destinatario = validarCorreo(email, evolucion.getUsuario().getEmail());

        try {
            servicioEmail.enviarEmailConAdjunto(
                    destinatario,
                    "PDF de Evolución",
                    "Por favor, revise el archivo adjunto correspondiente a la evolución.",
                    evolucion.getRutaPdf()
            );
            logger.info("Correo enviado a {} con el PDF de evolución ID: {}", destinatario, evolucionId);
        } catch (jakarta.mail.MessagingException e) {
            logger.error("Error al enviar el correo para la evolución {}: {}", evolucionId, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo: " + e.getMessage(), e);
        }
    }

    private Paciente obtenerPacientePorCuil(Long cuil) {
        return repositorioPaciente.buscarPorCuil(cuil)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuil));
    }

    private Diagnostico obtenerDiagnosticoDePaciente(Paciente paciente, Long diagnosticoId) {
        return paciente.obtenerDiagnosticoPorId(diagnosticoId);
    }

    private Evolucion obtenerEvolucionPorId(Long cuilPaciente, Long diagnosticoId, Long evolucionId) {
        return obtenerEvolucionesDelDiagnostico(cuilPaciente, diagnosticoId).stream()
                .filter(e -> e.getId().equals(evolucionId))
                .findFirst()
                .orElseThrow(() -> new EvolucionNoEncontradaException("Evolución no encontrada."));
    }

    private void validarMedico(Usuario medico) {
        if (medico == null) {
            throw new UsuarioNoAutenticadoException("El médico autenticado es obligatorio.");
        }
    }

    // Validar medicamentos utilizando el ServicioAPISalud
    private void validarMedicamentos(List<String> nombresMedicamentos) {
        List<Medicamento> medicamentosDisponibles = new ArrayList<>();
        boolean apiResult = servicioAPISalud.obtenerMedicamentos(medicamentosDisponibles);

        if (!apiResult || medicamentosDisponibles.isEmpty()) {
            throw new MedicamentoInvalidoException("No se pudo obtener la lista de medicamentos de la API de salud.");
        }

        // Lista de nombres válidos desde la API
        List<String> nombresValidos = medicamentosDisponibles.stream()
                .map(Medicamento::getNombre)
                .collect(Collectors.toList());

        // Validar que todos los medicamentos ingresados estén en la lista válida
        for (String nombre : nombresMedicamentos) {
            if (!nombresValidos.contains(nombre)) {
                throw new MedicamentoInvalidoException("El medicamento " + nombre + " no es válido.");
            }
        }
    }

    private String validarCorreo(String correo, String correoAlternativo) {
        String destinatario = (correo != null && !correo.isEmpty()) ? correo : correoAlternativo;

        if (destinatario == null || destinatario.isEmpty() || !destinatario.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new CorreoInvalidoException("No se especificó un correo electrónico válido.");
        }
        return destinatario;
    }

    private PlantillaControl convertirPlantillaControlDTO(PlantillaControlDTO dto) {
        if (dto == null) return null;
        return new PlantillaControl(dto.getPeso(), dto.getAltura(), dto.getPresion(), dto.getPulso(), dto.getSaturacion(), dto.getNivelAzucar());
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) return null;
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems(), dto.getEstado());
    }

    // Crear un MedicamentoRecetado desde un nombre de medicamento validado
    public MedicamentoRecetado crearMedicamentoRecetado(String nombreMedicamento) {
        return new MedicamentoRecetado(nombreMedicamento);
    }

}
