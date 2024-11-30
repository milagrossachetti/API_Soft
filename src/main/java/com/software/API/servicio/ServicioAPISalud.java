package com.software.API.servicio;

import com.software.API.modelo.Medicamento;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ServicioAPISalud {

    boolean verificarObraSocial(String codigo);

    boolean verificarMedicamentos(List<String> medicamentos);

    List<Medicamento> obtenerTodosLosMedicamentos(int pagina, int limite);
}