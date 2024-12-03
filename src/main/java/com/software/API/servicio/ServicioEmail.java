package com.software.API.servicio;

import jakarta.mail.MessagingException;

public interface ServicioEmail {

    void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException;
}