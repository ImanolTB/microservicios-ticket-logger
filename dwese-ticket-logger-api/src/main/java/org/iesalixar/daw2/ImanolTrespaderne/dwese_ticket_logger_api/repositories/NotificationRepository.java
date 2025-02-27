package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
}
