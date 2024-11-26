package com.software.API.modelo;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorPDF {

    private static final String OUTPUT_DIR = "output/";

    public static String generarPdfLaboratorio(Evolucion evolucion) throws Exception {
        if (evolucion == null) {
            throw new IllegalArgumentException("La evolución no puede ser nula.");
        }

        String pdfPath = OUTPUT_DIR + "pedido_laboratorio_" + evolucion.getId() + ".pdf";
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paciente paciente = evolucion.getDiagnostico().getHistoriaClinica().getPaciente();
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no está definido en esta evolución.");
        }

        document.add(new Paragraph("Datos del Paciente:"));
        document.add(new Paragraph("Nombre: " + paciente.getNombreCompleto()));
        document.add(new Paragraph("Obra Social: " + paciente.getObraSocialId()));
        document.add(new Paragraph("Número de Afiliado: " + paciente.getNroAfiliado()));
        document.add(new Paragraph("DNI: " + paciente.getDni()));
        document.add(new Paragraph("CUIL: " + paciente.getCuil()));

        Usuario medico = evolucion.getUsuario();
        if (medico != null) {
            document.add(new Paragraph("Datos del Médico:"));
            document.add(new Paragraph("Nombre: " + medico.getNombreCompleto()));
            document.add(new Paragraph("Especialidad: " + medico.getEspecialidad()));
        }

        String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        document.add(new Paragraph("Fecha de emisión: " + fechaFormateada));

        PlantillaLaboratorio plantillaLaboratorio = evolucion.getPlantillaLaboratorio();
        if (plantillaLaboratorio != null && plantillaLaboratorio.getTiposEstudios() != null) {
            document.add(new Paragraph("Tipos de Estudio Solicitados:"));
            for (String tipoEstudio : plantillaLaboratorio.getTiposEstudios()) {
                document.add(new Paragraph("Tipo de Estudio: " + tipoEstudio));

                if (plantillaLaboratorio.getItems() != null) {
                    document.add(new Paragraph("Ítems del estudio:"));
                    plantillaLaboratorio.getItems().forEach(item -> document.add(new Paragraph("- " + item)));
                }
            }
        } else {
            document.add(new Paragraph("No se solicitaron estudios de laboratorio."));
        }

        document.close();
        return pdfPath;
    }

    public static String generarPdfReceta(Evolucion evolucion, Receta receta) throws Exception {
        if (evolucion == null || receta == null) {
            throw new IllegalArgumentException("Evolución y receta no pueden ser nulas.");
        }

        String pdfPath = OUTPUT_DIR + "receta_" + receta.getId() + ".pdf";
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paciente paciente = evolucion.getDiagnostico().getHistoriaClinica().getPaciente();
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no está definido en esta evolución.");
        }

        document.add(new Paragraph("Datos del Paciente:"));
        document.add(new Paragraph("Nombre: " + paciente.getNombreCompleto()));
        document.add(new Paragraph("Obra Social: " + paciente.getObraSocialId()));
        document.add(new Paragraph("Número de Afiliado: " + paciente.getNroAfiliado()));
        document.add(new Paragraph("DNI: " + paciente.getDni()));
        document.add(new Paragraph("CUIL: " + paciente.getCuil()));

        Usuario medico = receta.getMedico();
        if (medico != null) {
            document.add(new Paragraph("Datos del Médico:"));
            document.add(new Paragraph("Nombre: " + medico.getNombreCompleto()));
            document.add(new Paragraph("Especialidad: " + medico.getEspecialidad()));
        }

        String fechaFormateada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        document.add(new Paragraph("Fecha de emisión: " + fechaFormateada));

        List<MedicamentoRecetado> medicamentos = receta.getMedicamentos();
        if (medicamentos != null) {
            document.add(new Paragraph("Medicamentos Recetados:"));
            for (MedicamentoRecetado medicamento : medicamentos) {
                document.add(new Paragraph("- " + medicamento.getNombre()));
            }
        } else {
            document.add(new Paragraph("No se encontraron medicamentos en esta receta."));
        }

        document.close();
        return pdfPath;
    }
}
