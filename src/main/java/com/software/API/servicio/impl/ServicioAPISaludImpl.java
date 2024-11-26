package com.software.API.servicio.impl;

import com.software.API.modelo.Medicamento;
import com.software.API.servicio.ServicioAPISalud;
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
    public boolean verificarObraSocial(Long id) {
        String url = apiBaseUrl + "/obras-sociales/" + id;

        try {
            Boolean respuesta = restTemplate.getForObject(url, Boolean.class);
            return respuesta != null && respuesta;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verificarNumeroAfiliado(Long id, String nroAfiliado) {
        String url = apiBaseUrl + "/obras-sociales/" + id + "/afiliados/" + nroAfiliado;

        try {
            Boolean respuesta = restTemplate.getForObject(url, Boolean.class);
            return respuesta != null && respuesta;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean obtenerMedicamentos(List<Medicamento> medicamentoList) {
        String url = apiBaseUrl + "/medicamentos";

        try {
            Medicamento[] medicamentos = restTemplate.getForObject(url, Medicamento[].class);

            if (medicamentos != null) {
                medicamentoList.addAll(Arrays.asList(medicamentos));
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
