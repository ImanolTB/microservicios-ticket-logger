package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.TicketDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class TicketMapper {
    @Autowired
    private LocationMapper locationMapper;

    public TicketDTO toDTO(Ticket ticket) {

        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setDate(ticket.getDate());
        dto.setDiscount(ticket.getDiscount());
        dto.setLocation(locationMapper.toDTO(ticket.getLocation()));
        return dto;
    }

    // Convierte el DTO a entidad Ticket
    public Ticket toEntity(TicketDTO dto) {
     Ticket ticket=new Ticket();
     ticket.setId(dto.getId());
     ticket.setDate(dto.getDate());
     ticket.setDiscount(dto.getDiscount());
     ticket.setLocation(locationMapper.toEntity(dto.getLocation()));
     return ticket;

    }
}
