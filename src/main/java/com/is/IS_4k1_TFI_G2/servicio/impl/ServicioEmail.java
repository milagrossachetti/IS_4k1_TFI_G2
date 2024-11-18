package com.is.IS_4k1_TFI_G2.servicio.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
public class ServicioEmail {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarPdfPorEmail(String emailDestino, String asunto, String cuerpo, String rutaPdf) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true); // true para multipart (para adjuntar archivos)

        helper.setTo(emailDestino);
        helper.setSubject(asunto);
        helper.setText(cuerpo, true);

        File archivo = new File(rutaPdf);
        if (archivo.exists() && archivo.isFile()) {
            FileSystemResource archivoPdf = new FileSystemResource(archivo);
            helper.addAttachment(archivoPdf.getFilename(), archivoPdf);
        } else {
            throw new MessagingException("El archivo PDF no existe o la ruta es incorrecta.");
        }

        mailSender.send(mensaje);
    }
    public void enviarPdfPorEmailConAdjuntos(String emailDestino, String asunto, String cuerpo, List<String> rutasPdf) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true); // true para multipart (para adjuntar archivos)

        helper.setTo(emailDestino);
        helper.setSubject(asunto);
        helper.setText(cuerpo, true);

        for (String rutaPdf : rutasPdf) {
            File archivo = new File(rutaPdf);
            if (archivo.exists() && archivo.isFile()) {
                FileSystemResource archivoPdf = new FileSystemResource(archivo);
                helper.addAttachment(archivoPdf.getFilename(), archivoPdf);
            } else {
                throw new MessagingException("El archivo PDF no existe o la ruta es incorrecta: " + rutaPdf);
            }
        }

        mailSender.send(mensaje);
    }
}