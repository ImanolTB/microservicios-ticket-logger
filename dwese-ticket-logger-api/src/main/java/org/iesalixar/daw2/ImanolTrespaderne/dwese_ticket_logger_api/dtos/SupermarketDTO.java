package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SupermarketDTO {
    private Long id;
    private String name;
    private List<LocationDTO> locations;
}
