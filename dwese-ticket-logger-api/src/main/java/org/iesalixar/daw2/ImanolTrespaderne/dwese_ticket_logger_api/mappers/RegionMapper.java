package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Region;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper {
    public RegionDTO toDTO(Region region){
        RegionDTO dto= new RegionDTO();
        dto.setId(region.getId());
        dto.setCode(region.getCode());
        dto.setName(region.getName());
        return dto;
    }
    public Region toEntity(RegionDTO dto){
        Region region = new Region();
        region.setId(dto.getId());
        region.setCode(dto.getCode());
        region.setName(dto.getName());
        return region;
    }
}
