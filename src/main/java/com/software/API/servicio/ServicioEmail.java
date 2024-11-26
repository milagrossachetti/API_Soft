package com.software.API.servicio;

import jakarta.mail.MessagingException;

public interface ServicioEmail {

    /**
     * Envía un email con un archivo PDF adjunto.
     *
     * @param emailDestino Dirección de correo electrónico del destinatario.
     * @param asunto Asunto del correo electrónico.
     * @param cuerpo Cuerpo del mensaje del correo electrónico. Puede incluir HTML.
     * @param rutaPdf Ruta al archivo PDF que se adjuntará al correo.
     * @throws MessagingException Si ocurre un error al enviar el correo o al adjuntar el archivo.
     */
    void enviarEmailConAdjunto(String emailDestino, String asunto, String cuerpo, String rutaPdf) throws MessagingException;
}
