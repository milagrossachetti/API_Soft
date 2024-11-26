package com.software.API.servicio.impl;


import com.software.API.servicio.ServicioEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class ServicioEmailImpl implements ServicioEmail {

    @Autowired
    private JavaMailSender mailSender;

    // Método para enviar un email con un archivo PDF adjunto
    @Override
    public void enviarEmailConAdjunto(String emailDestino, String asunto, String cuerpo, String rutaPdf) throws MessagingException {
        // Crear el mensaje MIME
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true); // true para multipart (para adjuntar archivos)

        // Configurar los detalles del email
        helper.setTo(emailDestino);
        helper.setSubject(asunto);
        helper.setText(cuerpo, true); // true para permitir HTML en el cuerpo del email

        // Adjuntar el archivo PDF
        File archivo = new File(rutaPdf);
        if (archivo.exists() && archivo.isFile()) {
            FileSystemResource archivoPdf = new FileSystemResource(archivo);
            helper.addAttachment(archivoPdf.getFilename(), archivoPdf);
        } else {
            throw new MessagingException("El archivo PDF no existe o la ruta es incorrecta.");
        }

        // Enviar el mensaje
        mailSender.send(mensaje);
    }
}
