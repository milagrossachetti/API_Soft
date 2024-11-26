package com.software.API.servicio;

import com.software.API.modelo.Medicamento;

import java.util.List;

public interface ServicioAPISalud {

    /**
     * Verifica si una obra social existe en el sistema.
     *
     * @param id Identificador único de la obra social.
     * @return `true` si la obra social existe, `false` en caso contrario.
     */
    boolean verificarObraSocial(Long id);

    /**
     * Verifica si un número de afiliado está asociado a una obra social específica.
     *
     * @param id Identificador único de la obra social.
     * @param nroAfiliado Número de afiliado a verificar.
     * @return `true` si el número de afiliado es válido para la obra social, `false` en caso contrario.
     */
    boolean verificarNumeroAfiliado(Long id, String nroAfiliado);

    /**
     * Obtiene una lista de medicamentos disponibles desde la API externa y los agrega a la lista proporcionada.
     *
     * @param medicamentoList Lista donde se agregarán los medicamentos obtenidos de la API.
     * @return `true` si se obtuvieron medicamentos exitosamente, `false` en caso contrario.
     */
    boolean obtenerMedicamentos(List<Medicamento> medicamentoList);
}
