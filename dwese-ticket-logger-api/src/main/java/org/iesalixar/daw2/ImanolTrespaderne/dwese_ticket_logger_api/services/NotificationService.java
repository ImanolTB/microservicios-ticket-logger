package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers.RegionController;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.NotificationCreateDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.NotificationDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Notification;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers.NotificationMapper;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.NotificationRepository;
import org.jetbrains.annotations.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    public Mono<NotificationDTO> saveNotification (NotificationCreateDTO notificationCreateDTO){
        Notification notification= NotificationMapper.toEntity(notificationCreateDTO);
        return notificationRepository.save(notification)
                //Se ejecuta cuando la notificacion ha sido guardada.
                .doOnSuccess(savedNotification ->
                        //Crea un Mono para ejecutar la tarea de enviar la notificacion por WebSocket
                        Mono.fromRunnable( () -> messagingTemplate.convertAndSend(
                                "/topic/notifications", //Canal donde se enviará la notificación
                                NotificationMapper.toDTO(savedNotification)
                        ))
                                .subscribeOn(Schedulers.boundedElastic()) //Se ejecuta en un pool de hilos elásticos para evitar bloqueos
                                .subscribe()) //Subscripcion para ejecutar la tarea asíncrina de envío
                                .map(NotificationMapper::toDTO);

    }

    public Flux<NotificationDTO> getAllNotifications(){
        return notificationRepository.findAll().map(NotificationMapper::toDTO);
    }
}
