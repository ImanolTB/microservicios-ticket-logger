package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Location;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Supermarket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


public class SupermarketMapper {
    @Autowired
    private LocationMapper locationMapper;
    public SupermarketDTO toDTO(Supermarket supermarket) {

        SupermarketDTO dto = new SupermarketDTO();
        dto.setId(supermarket.getId());
        dto.setName(supermarket.getName());
        //lista de locations

        return dto;
    }

    // Mapeo de SupermarketDTO a Supermarket
    public  Supermarket toEntity(SupermarketDTO dto) {


        Supermarket supermarket = new Supermarket();
        supermarket.setId(dto.getId());
        supermarket.setName(dto.getName());
        // Aquí mapeamos la lista de locations, si es necesario.

        return supermarket;
    }
}

