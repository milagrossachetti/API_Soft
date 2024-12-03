package com.software.API.servicio;

import com.software.API.DTOs.EvolucionDTO;
import com.software.API.modelo.Evolucion;
import com.software.API.modelo.Paciente;
import com.software.API.modelo.Receta;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ServicioEvolucion {

    /**
     * Obtiene las evoluciones asociadas a un diagnóstico específico de un paciente.
     *
     * @param cuilPaciente CUIL del paciente.
     * @param diagnosticoId ID del diagnóstico.
     * @return Lista de evoluciones asociadas al diagnóstico.
     */
    List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId);

    /**
     * Crea una nueva evolución asociada a un diagnóstico y la guarda en el paciente.
     *
     * @param cuilPaciente CUIL del paciente.
     * @param diagnosticoId ID del diagnóstico.
     * @param evolucionDTO Datos de la evolución a crear.
     * @param nombreMedico Médico que crea la evolución.
     * @param especialidadMedico
     * @return La evolución creada.
     */
    Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, String nombreMedico , String especialidadMedico);

    /**
     * Crea una receta asociada a una evolución específica.
     *
     * @param nombresMedicamentos Lista de nombres de medicamentos a incluir en la receta.
     * @param evolucionId Id Evolución a la que se asocia la receta.
     * @param nombreMedico Médico que crea la receta.
     * @param especialidadMedico
     * @return La receta creada.
     */
    Receta crearReceta(List<String> nombresMedicamentos, Long diagnosticoId, Long evolucionId, Paciente paciente, String nombreMedico, String especialidadMedico);

    /**
     * Genera un PDF para una receta médica.
     *
     * @param numeroReceta       Número de la receta.
     * @param medicamentos       Lista de medicamentos incluidos en la receta.
     * @param nombrePaciente     Nombre del paciente.
     * @param nombreMedico       Nombre del médico.
     * @param especialidadMedico Especialidad del médico.
     * @param obraSocial
     * @param nroAfiliado
     * @return Un array de bytes que representa el PDF generado.
     */
    byte[] generarPDFReceta(Long numeroReceta, List<String> medicamentos, String nombrePaciente, String nombreMedico, String especialidadMedico, String obraSocial, String nroAfiliado);


    /**
     * Genera un PDF para un pedido de laboratorio.
     *
     * @param nombrePaciente Nombre del paciente.
     * @param nombreMedico Nombre del médico.
     * @param especialidadMedico Especialidad del médico.
     * @param tiposEstudios Lista de tipos de estudios solicitados.
     * @param items Lista de ítems solicitados.
     *  @param obraSocial
     * @param nroAfiliado
     * @return Un array de bytes que representa el PDF generado.
     */
    byte[] generarPDFLaboratorio(String nombrePaciente, String nombreMedico, String especialidadMedico, List<String> tiposEstudios, List<String> items, String obraSocial, String nroAfiliado);

    /**
     * Envía un correo electrónico con un archivo adjunto.
     *
     * @param destinatario Dirección de correo del destinatario.
     * @param asunto Asunto del correo.
     * @param cuerpo Cuerpo del correo.
     * @param adjunto Archivo adjunto en formato de array de bytes.
     * @param nombreAdjunto Nombre del archivo adjunto.
     * @throws MessagingException Si ocurre un error al enviar el correo.
     */
    void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException;
}
