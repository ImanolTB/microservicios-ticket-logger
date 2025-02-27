package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.RabbitMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/rabbitmq")
public class RabbitMQController {
    @Autowired
    private RabbitMQProducerService rabbitMQProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message){
        rabbitMQProducer.sendMessage(message);
        return "Mensaje enviado a RabbitMQ: "+ message;
    }

}
