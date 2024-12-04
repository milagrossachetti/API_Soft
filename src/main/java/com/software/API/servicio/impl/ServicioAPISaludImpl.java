package com.software.API.servicio.impl;

import com.software.API.modelo.Medicamento;
import com.software.API.servicio.ServicioAPISalud;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.software.API.modelo.ObraSocial;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
@Service
public class ServicioAPISaludImpl implements ServicioAPISalud {

    private final RestTemplate restTemplate;

    @Value("${api.salud.base-url}")
    private String apiBaseUrl;

    public ServicioAPISaludImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<Medicamento> obtenerTodosLosMedicamentos(int pagina, int limite) {
        if (pagina < 1 || limite < 1 || limite > 1000) {
            throw new IllegalArgumentException("Parámetros de paginación inválidos. Página y límite deben ser positivos, y límite no mayor a 1000.");
        }

        String url = apiBaseUrl + "/medicamentos/todos?pagina=" + pagina + "&limite=" + limite;
        try {
            Medicamento[] medicamentos = restTemplate.getForObject(url, Medicamento[].class);
            return medicamentos != null ? Arrays.asList(medicamentos) : List.of();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los medicamentos: " + e.getMessage(), e);
        }
    }

    public Medicamento obtenerMedicamento(int codigo) {
        try {
            return restTemplate.getForObject(apiBaseUrl + "/medicamentos/" + codigo, Medicamento.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el medicamento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Medicamento> obtenerMedicamentosPorDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser vacía o nula.");
        }

        String encodedDescripcion = URLEncoder.encode(descripcion, StandardCharsets.UTF_8);
        String url = apiBaseUrl + "/medicamentos?descripcion=" + encodedDescripcion;

        // Log para verificar la URL antes de la llamada
        System.out.println("URL de la consulta: " + url);

        try {
            Medicamento[] medicamentos = restTemplate.getForObject(url, Medicamento[].class);

            // Verificar si la respuesta es válida y si contiene medicamentos
            if (medicamentos != null && medicamentos.length > 0) {
                return Arrays.asList(medicamentos);
            } else {
                return List.of();  // Si no hay medicamentos, devolver lista vacía
            }
        } catch (Exception e) {
            // Log de error detallado
            System.out.println("Error al obtener medicamentos: " + e.getMessage());
            throw new RuntimeException("Error al obtener los medicamentos por descripción: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ObraSocial> obtenerTodasLasObrasSociales() {
        String url = apiBaseUrl + "/obras-sociales";
        try {
            ObraSocial[] obrasSociales = restTemplate.getForObject(url, ObraSocial[].class);
            return obrasSociales != null ? Arrays.asList(obrasSociales) : List.of();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las obras sociales: " + e.getMessage(), e);
        }
    }

    @Override
    public ObraSocial obtenerObraSocialPorCodigo(String codigo) {
        String url = apiBaseUrl + "/obras-sociales/" + codigo;
        try {
            return restTemplate.getForObject(url, ObraSocial.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la obra social: " + e.getMessage(), e);
        }
    }


}