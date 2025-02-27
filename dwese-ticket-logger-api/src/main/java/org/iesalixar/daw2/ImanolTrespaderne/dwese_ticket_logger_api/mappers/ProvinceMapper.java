package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Province;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProvinceMapper {
    @Autowired
    private RegionMapper regionMapper;
    public ProvinceDTO toDTO(Province province){
        ProvinceDTO dto= new ProvinceDTO();
        dto.setId(province.getId());
        dto.setCode(province.getCode());
        dto.setName(province.getName());
        dto.setRegion(regionMapper.toDTO(province.getRegion()));
        return dto;
    }
    public Province toEntity(ProvinceDTO dto){
        Province province= new Province();
        province.setId(dto.getId());
        province.setCode(dto.getCode());
        province.setName(dto.getName());
        province.setRegion(regionMapper.toEntity(dto.getRegion()));
        return province;
    }
}
