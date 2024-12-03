package com.software.API.servicio.impl;

import com.software.API.servicio.ServicioEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.util.ByteArrayDataSource;



@Service
public class ServicioEmailImpl implements ServicioEmail {

    private final JavaMailSender mailSender;

    public ServicioEmailImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(cuerpo);
        helper.addAttachment(nombreAdjunto, new ByteArrayDataSource(adjunto, "application/pdf"));

        mailSender.send(message);
    }
}
