package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender emailSender;
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail( @RequestParam String usermail) {
        try {
            emailService.sendSimpleMessage(usermail);
            return ResponseEntity.ok("Correo enviado con éxito a " + usermail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo");
        }
    }
}
