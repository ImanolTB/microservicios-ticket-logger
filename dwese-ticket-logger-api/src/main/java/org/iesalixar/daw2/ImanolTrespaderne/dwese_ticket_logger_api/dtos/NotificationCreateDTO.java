package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateDTO {
    private String subject;

    private String message;

    private boolean read;
}
