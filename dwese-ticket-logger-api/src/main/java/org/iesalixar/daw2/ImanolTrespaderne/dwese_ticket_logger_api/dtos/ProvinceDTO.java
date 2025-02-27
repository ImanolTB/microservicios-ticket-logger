package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProvinceDTO {
    private Long id;
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2,message = "{msg.province.code.size}")
    private String code;
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size (max = 100,message = "{msg.province.name.size}")
    private String name;
    @NotNull(message = "{msg.province.region.notNull}")
    private RegionDTO region;
}
