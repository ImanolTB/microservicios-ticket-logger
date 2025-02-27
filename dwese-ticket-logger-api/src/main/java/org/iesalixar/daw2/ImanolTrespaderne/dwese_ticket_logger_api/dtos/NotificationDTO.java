package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String id;

    private String subject;

    private String message;

    private boolean read;

    private Instant createdAt ;
}
