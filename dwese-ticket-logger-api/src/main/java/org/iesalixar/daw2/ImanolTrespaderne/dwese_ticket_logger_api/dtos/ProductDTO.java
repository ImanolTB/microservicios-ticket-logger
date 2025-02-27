package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private CategoryDTO category;
}
