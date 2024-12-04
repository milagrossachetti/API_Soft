package com.software.API.servicio.impl;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.software.API.servicio.ServicioPDF;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioPDFImpl implements ServicioPDF {

    @Override
    public byte[] generarPDFReceta(Long numeroReceta, List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico, String obraSocial, String nroAfiliado) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Contenido del PDF
        document.add(new Paragraph("Policlinica MARB."));
        document.add(new Paragraph("Rivadavia 1050, San Miguel de Tucumán, Tucumán\nTel: 3815273400"));

        LineSeparator ls = new LineSeparator(new SolidLine());
        ls.setWidth(UnitValue.createPercentValue(100));
        document.add(new Paragraph("\n")).add(ls).add(new Paragraph("\n"));

        document.add(new Paragraph("\nFecha Receta: " + LocalDate.now()).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Receta N°: " + numeroReceta).setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\nBeneficiario: " + nombrePaciente));
        document.add(new Paragraph("Cobertura: " + obraSocial));
        document.add(new Paragraph("N° Afiliado: " + nroAfiliado));
        document.add(new Paragraph("\nRp/").setFontSize(12).setMarginTop(10).setBold());
        for (String medicamento : medicamentos) {
            document.add(new Paragraph(medicamento).setTextAlignment(TextAlignment.CENTER));
        }
        document.add(new Paragraph("\nFirmado electrónicamente por:").setBold());
        document.add(new Paragraph("Dra./Dr. " + nombreMedico));
        document.add(new Paragraph("Especialidad: " + especialidadMedico));

        document.close();
        return baos.toByteArray();
    }

    @Override
    public byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items, String obraSocial, String nroAfiliado) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Contenido del PDF
        document.add(new Paragraph("Policlinica MARB."));
        document.add(new Paragraph("Rivadavia 1050, San Miguel de Tucumán, Tucumán\nTel: 3815273400"));

        LineSeparator ls = new LineSeparator(new SolidLine());
        ls.setWidth(UnitValue.createPercentValue(100));
        document.add(new Paragraph("\n")).add(ls).add(new Paragraph("\n"));

        document.add(new Paragraph("\nFecha: " + LocalDate.now()).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Pedido de Laboratorio").setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\nBeneficiario: " + nombrePaciente));
        document.add(new Paragraph("Cobertura: " + obraSocial));
        document.add(new Paragraph("N° Afiliado: " + nroAfiliado));
        document.add(new Paragraph("\nEstudios Solicitados:").setFontSize(12).setMarginTop(10).setBold());
        for (String estudio : tiposEstudios) {
            document.add(new Paragraph(estudio).setTextAlignment(TextAlignment.CENTER));
        }

        document.add(new Paragraph("\nItems:").setFontSize(12).setMarginTop(10).setBold());
        for (String item : items) {
            document.add(new Paragraph(item).setTextAlignment(TextAlignment.CENTER));
        }

        document.add(new Paragraph("\nFirmado electrónicamente por:").setBold());
        document.add(new Paragraph("Dra./Dr. " + nombreMedico));
        document.add(new Paragraph("Especialidad: " + especialidadMedico));

        document.close();
        return baos.toByteArray();
    }
}