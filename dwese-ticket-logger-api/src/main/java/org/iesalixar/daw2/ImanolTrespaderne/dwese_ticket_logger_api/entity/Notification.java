package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;

    private String subject;

    private String message;

    private boolean read;

    private Instant createdAt = Instant.now();

}
