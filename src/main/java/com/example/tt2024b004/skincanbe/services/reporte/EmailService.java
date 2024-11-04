/**
 ********************************
 ** Proyecto: Skincanbe        **
 ** Integrantes:               **
 ** Joan Hanzka Manuel Morales **
 ** Angelo Mihaelle Ojeda Gomez**
 ** Israel Rodriguez Juarez     **
 ******************************** 
 * Descripción: Clase servicio para implementar el envio de correos electronicos.
 */
package com.example.tt2024b004.skincanbe.services.reporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String text, String filename) throws MessagingException {
        System.out.println("Entre al metodo sendEmailWithAttachment del servicio EmailService");
        System.out.println(to);
        System.out.println(subject);
        System.out.println(text);
        System.out.println(filename);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        FileSystemResource fileResource = new FileSystemResource(filename);

        helper.addAttachment(fileResource.getFilename(), fileResource);

        mailSender.send(message);
        System.out.println("Se mando el correo");
    }
}

