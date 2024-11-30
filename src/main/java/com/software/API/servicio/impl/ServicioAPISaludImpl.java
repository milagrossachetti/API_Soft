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
    public boolean verificarObraSocial(String codigo) {
        String url = apiBaseUrl + "/obras-sociales/" + codigo;

        try {
            Boolean respuesta = restTemplate.getForObject(url, Boolean.class);
            return respuesta != null && respuesta;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verificarMedicamentos(List<String> medicamentos) {
        String url = apiBaseUrl + "/medicamentos";
        for (String medicamento : medicamentos) {
            try {
                Boolean respuesta = restTemplate.getForObject(url + "/" + medicamento, Boolean.class);
                if (respuesta == null || !respuesta) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Medicamento> obtenerTodosLosMedicamentos(int pagina, int limite) {
        String url = apiBaseUrl + "/medicamentos/todos?pagina=" + pagina + "&limite=" + limite;
        try {
            Medicamento[] medicamentos = restTemplate.getForObject(url, Medicamento[].class);
            return medicamentos != null ? Arrays.asList(medicamentos) : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
}