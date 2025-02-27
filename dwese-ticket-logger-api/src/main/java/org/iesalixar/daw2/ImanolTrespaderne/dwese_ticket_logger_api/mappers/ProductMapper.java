package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.ProductDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class ProductMapper {
    @Autowired
    private CategoryMapper categoryMapper;
    public ProductDTO toDTO(Product product) {
        ProductDTO dto= new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(categoryMapper.toDTO(product.getCategory()));
        dto.setPrice(product.getPrice());
        return dto;
    }

    // Convierte el DTO a entidad Product
    public Product toEntity(ProductDTO dto) {
        Product product= new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setCategory(categoryMapper.toEntity2(dto.getCategory()));
        product.setPrice(dto.getPrice());
        return product;
    }
}
