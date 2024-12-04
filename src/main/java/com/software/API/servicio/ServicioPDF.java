package com.software.API.servicio;

import java.util.List;

public interface ServicioPDF {
    byte[] generarPDFReceta(Long numeroReceta, List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico, String obraSoacial, String nroAfiliado);

    byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items, String obraSocial, String nroAfiliado);
}