package com.software.API.servicio;

import com.software.API.modelo.Medicamento;
import com.software.API.modelo.Medicamento;
import com.software.API.modelo.ObraSocial;

import java.util.List;

public interface ServicioAPISalud {

    List<Medicamento> obtenerTodosLosMedicamentos(int pagina, int limite);

    Medicamento obtenerMedicamento(int codigo);

    List<Medicamento> obtenerMedicamentosPorDescripcion(String descripcion);

    List<ObraSocial> obtenerTodasLasObrasSociales();

    ObraSocial obtenerObraSocialPorCodigo(String codigo);
}
