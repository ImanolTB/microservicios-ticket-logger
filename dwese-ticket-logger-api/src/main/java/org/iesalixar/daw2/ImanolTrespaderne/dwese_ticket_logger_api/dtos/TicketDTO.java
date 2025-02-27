package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TicketDTO {

    private Long id;
    private Date date;
    private BigDecimal discount;
    private LocationDTO location;
}
