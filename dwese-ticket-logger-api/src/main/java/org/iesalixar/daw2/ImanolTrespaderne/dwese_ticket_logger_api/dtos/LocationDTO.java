package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Long id;
    private String address;
    private String city;
    private SupermarketDTO supermarket;
    private ProvinceDTO province;


}
