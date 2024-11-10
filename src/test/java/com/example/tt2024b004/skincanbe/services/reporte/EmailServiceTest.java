package com.example.tt2024b004.skincanbe.services.reporte;

import static org.mockito.Mockito.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmailWithAttachment() throws MessagingException,IOException{
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";
        String filename = "test.pdf";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper helper = mock(MimeMessageHelper.class);

        
        emailService.sendEmailWithAttachment(to, subject, text, filename);

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }
}

