package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.config.RabbitMQConfig.QUEUE_NAME;

@Service
public class RabbitMQProducerService {

    private static final Logger logger= LoggerFactory.getLogger(RabbitMQProducerService.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        logger.info("Mensaje enviado a RabbitMQ: {}", message);
    }
}
