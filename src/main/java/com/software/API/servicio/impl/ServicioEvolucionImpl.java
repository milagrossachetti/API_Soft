package com.software.API.servicio.impl;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.software.API.DTOs.*;
import com.software.API.excepcion.*;
import com.software.API.modelo.*;
import com.software.API.repositorio.*;
import com.software.API.repositorio.RepositorioAPISalud;
import com.software.API.servicio.ServicioAPISalud;
import com.software.API.servicio.ServicioEvolucion;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicioEvolucionImpl implements ServicioEvolucion {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioAPISalud servicioAPISalud;
    private final RepositorioAPISalud repositorioAPISalud;
    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucionImpl(RepositorioPaciente repositorioPaciente, ServicioAPISalud servicioAPISalud, RepositorioAPISalud repositorioAPISalud, JavaMailSender mailSender) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioAPISalud = servicioAPISalud;
        this.repositorioAPISalud = repositorioAPISalud;
        this.mailSender = mailSender;
    }

    // Obtener evoluciones de un diagnóstico específico
    @Override
    public List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        Diagnostico diagnostico = paciente.obtenerDiagnosticoPorId(diagnosticoId);
        return diagnostico.getEvoluciones();
    }

    // Crear evolución para un diagnóstico
    @Override
    public Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, String nombreMedico, String especialidadMedico) {
        if (evolucionDTO == null) {
            throw new IllegalArgumentException("Los datos de la evolución no pueden ser nulos.");
        }

        // Validar que al menos uno de los campos esté presente
        boolean tieneContenido = (evolucionDTO.getTexto() != null && !evolucionDTO.getTexto().isEmpty()) ||
                evolucionDTO.getPlantillaControl() != null ||
                evolucionDTO.getPlantillaLaboratorio() != null ||
                (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty());

        if (!tieneContenido) {
            throw new IllegalArgumentException("La evolución debe tener al menos texto, plantilla de control, plantilla de laboratorio o una receta.");
        }

        boolean tieneRecetaYLaboratorio = (evolucionDTO.getPlantillaLaboratorio() != null &&
                (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()));

        if(tieneRecetaYLaboratorio){
            throw new IllegalArgumentException("La evolucion no puede tener receta y plantilla de laboratorio al mismo tiempo.");
        }

        if (nombreMedico == null || nombreMedico.isEmpty() || especialidadMedico == null || especialidadMedico.isEmpty()) {
            throw new IllegalArgumentException("El nombre y la especialidad del médico son obligatorios.");
        }

        // Obtener el paciente de manera encapsulada
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente);
        }

        // Crear la evolución a través del flujo jerárquico
        Evolucion nuevaEvolucion = paciente.crearYAgregarEvolucion(
                diagnosticoId,
                evolucionDTO.getTexto(),
                nombreMedico,
                especialidadMedico,
                convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl()),
                convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio())
        );

        // Manejar las recetas si están presentes en el DTO
        if (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()) {
            for (RecetaDTO recetaDTO : evolucionDTO.getRecetas()) {
                List<String> medicamentos = recetaDTO.getMedicamentos();
                if (medicamentos == null || medicamentos.isEmpty()) {
                    throw new IllegalArgumentException("Las recetas deben contener al menos un medicamento.");
                }

                Receta receta = crearReceta(
                        medicamentos,
                        diagnosticoId,
                        nuevaEvolucion.getId(),
                        paciente,
                        nombreMedico,
                        especialidadMedico
                );

                // Generar PDF de la receta
                byte[] pdfReceta = generarPDFReceta(receta.getId(), medicamentos, paciente.getNombreCompleto(), nombreMedico, especialidadMedico);
                try {
                    enviarEmailConAdjunto("cisterna2728@gmail.com", "Receta Médica", "Adjunto encontrarás la receta médica.", pdfReceta, "receta.pdf");
                } catch (MessagingException e) {
                    logger.error("Error al enviar el correo: ", e);
                }
            }
        } else if (evolucionDTO.getPlantillaLaboratorio() != null) {
                byte[] pdfLaboratorio = generarPDFLaboratorio(paciente.getNombreCompleto(), nombreMedico, especialidadMedico, evolucionDTO.getPlantillaLaboratorio().getTiposEstudios(),  evolucionDTO.getPlantillaLaboratorio().getItems());
                try {
                    enviarEmailConAdjunto("cisterna2728@gmail.com", "Pedido de Laboratorio", "Adjunto encontrarás el pedido de laboratorio.", pdfLaboratorio, "laboratorio.pdf");
                } catch (MessagingException e) {
                    logger.error("Error al enviar el correo: ", e);
                }
        }

        // Guardar los cambios en el repositorio
        repositorioPaciente.guardarPaciente(paciente);

        // Registrar el evento en los logs
        logger.info("Evolución creada exitosamente para el paciente con CUIL: {}, diagnóstico ID: {}, por el médico: {} ({})",
                cuilPaciente, diagnosticoId, nombreMedico, especialidadMedico);


        // Retornar la evolución creada
        return nuevaEvolucion;
    }

    // Crear receta y asociarla a una evolución
    @Override
    public Receta crearReceta(List<String> nombresMedicamentos, Long diagnosticoId, Long evolucionId, Paciente paciente, String nombreMedico, String especialidadMedico) {
        // Validar la cantidad de medicamentos
        if (nombresMedicamentos == null || nombresMedicamentos.size() > 2) {
            throw new RecetaInvalidaException("Solo se permiten hasta 2 medicamentos por receta.");
        }

        // Validar medicamentos con la API de salud
        validarMedicamentos(nombresMedicamentos);

        // Delegar la creación de la receta al flujo jerárquico desde paciente
        Receta receta = paciente.crearReceta(
                nombresMedicamentos, // Lista de medicamentos
                diagnosticoId,       // ID del diagnóstico
                evolucionId,         // ID de la evolución
                nombreMedico,        // Nombre del médico
                especialidadMedico   // Especialidad del médico
        );

        // Loguear la creación exitosa
        logger.info("Receta creada con medicamentos: {}", nombresMedicamentos);

        return receta;
    }


    private Paciente obtenerPacientePorCuil(Long cuil) {
        return repositorioPaciente.buscarPorCuil(cuil)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuil));
    }


    // Validar medicamentos utilizando el ServicioAPISalud
    private void validarMedicamentos(List<String> nombresMedicamentos) {
        List<Medicamento> apiResult = repositorioAPISalud.obtenerMedicamentos();

        // Lista de nombres válidos desde la API
        List<String> nombresValidos = apiResult.stream()
                .map(Medicamento::getNombre)
                .collect(Collectors.toList());

        // Validar que todos los medicamentos ingresados estén en la lista válida
        for (String nombre : nombresMedicamentos) {
            if (!nombresValidos.contains(nombre)) {
                throw new MedicamentoInvalidoException("El medicamento " + nombre + " no es válido.");
            }
        }
    }

    private PlantillaControl convertirPlantillaControlDTO(PlantillaControlDTO dto) {
        if (dto == null) return null;
        return new PlantillaControl(dto.getPeso(), dto.getAltura(), dto.getPresion(), dto.getPulso(), dto.getSaturacion(), dto.getNivelAzucar());
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) return null;
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems());
    }

@Override
    public byte[] generarPDFReceta(Long numeroReceta, @NotNull List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Nombre de la Empresa."));
        document.add(new Paragraph("CABRERA 3314, Palermo, Ciudad de Buenos Aires\nTel: 115273400"));

        LineSeparator ls = new LineSeparator(new SolidLine());
        ls.setWidth(UnitValue.createPercentValue(100));
        document.add(new Paragraph("\n")).add(ls).add(new Paragraph("\n"));

        document.add(new Paragraph("\nFecha Receta: " + LocalDate.now())
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Receta N°: " + numeroReceta)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\nBeneficiario: " + nombrePaciente));
        document.add(new Paragraph("Cobertura: "));
        document.add(new Paragraph("N° Afiliado: " ));
        document.add(new Paragraph("\nRp/").setFontSize(12).setMarginTop(10).setBold());
        for (String medicamento : medicamentos) {
            document.add(new Paragraph(medicamento)
                    .setTextAlignment(TextAlignment.CENTER));
        }
        document.add(new Paragraph("\nFirmado electrónicamente por:").setBold());
        document.add(new Paragraph("Dra./Dr. " + nombreMedico));
        document.add(new Paragraph("Matrícula: "));
        document.add(new Paragraph("Especialidad: " + especialidadMedico));

        document.close();

        return baos.toByteArray();
    }

    @Override
    public byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Nombre de la Empresa."));
        document.add(new Paragraph("CABRERA 3314, Palermo, Ciudad de Buenos Aires\nTel: 115273400"));

        LineSeparator ls = new LineSeparator(new SolidLine());
        ls.setWidth(UnitValue.createPercentValue(100));
        document.add(new Paragraph("\n")).add(ls).add(new Paragraph("\n"));

        document.add(new Paragraph("\nFecha: " + LocalDate.now())
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Pedido de Laboratorio")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\nBeneficiario: " + nombrePaciente));
        document.add(new Paragraph("Cobertura: "));
        document.add(new Paragraph("N° Afiliado: "));

        document.add(new Paragraph("\nEstudios Solicitados:").setFontSize(12).setMarginTop(10).setBold());
        for (String estudio : tiposEstudios) {
            document.add(new Paragraph(estudio)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        document.add(new Paragraph("\nItems:").setFontSize(12).setMarginTop(10).setBold());
        for (String item : items) {
            document.add(new Paragraph(item)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        document.add(new Paragraph("\nFirmado electrónicamente por:").setBold());
        document.add(new Paragraph("Dra./Dr. " + nombreMedico));
        document.add(new Paragraph("Matrícula: "));
        document.add(new Paragraph("Especialidad: " + especialidadMedico));

        document.close();

        return baos.toByteArray();
    }

    public void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(cuerpo);
        helper.addAttachment(nombreAdjunto, new ByteArrayDataSource(adjunto, "application/pdf"));

        mailSender.send(message);
    }
}
