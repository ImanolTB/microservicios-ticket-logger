package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Location;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Province;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Supermarket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class LocationMapper {
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private SupermarketMapper supermarketMapper;
    public LocationDTO toDTO(Location location) {
        return new LocationDTO(
                location.getId(),
                location.getAddress(),
                location.getCity(),
                supermarketMapper.toDTO(location.getSupermarket()),
                provinceMapper.toDTO(location.getProvince())
        );
    }


    public Location toEntity(LocationDTO locationDTO) {

        Supermarket supermarket = supermarketMapper.toEntity(locationDTO.getSupermarket());
        Province province = provinceMapper.toEntity(locationDTO.getProvince());
        return new Location(
                locationDTO.getId(),
                locationDTO.getAddress(),
                locationDTO.getCity(),
                supermarket,
                province
        );
    }
}
