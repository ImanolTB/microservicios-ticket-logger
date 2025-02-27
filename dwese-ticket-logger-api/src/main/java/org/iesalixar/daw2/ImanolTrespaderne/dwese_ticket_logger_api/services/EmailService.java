package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class EmailService {
    @Value("${email.username}")
    private String email;
    @Autowired
    private JavaMailSender emailSender;



    public void sendSimpleMessage( String usermail) {
          String subject="Prueba";
         String text="Mensaje de de prueba ";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(usermail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}