package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegionDTO {
    private Long id;
    @NotEmpty(message = "{msg.region.code.notEmpty}")
    @Size (max = 2,message = "{msg.region.code.size}")
    private String code;
    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size (max = 100, message = "{msg.region.name.size}")
    private String name;
}
