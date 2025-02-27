package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.category.name.notEmpty}")
    @Size(max = 100, message = "{msg.category.name.size}") // Ajustar max a 100 o a lo que necesites
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100, message = "{msg.category.image.size}")
    @Column(name = "image", nullable = true, length = 100) // Mantener nullable si es opcional
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true) // Cambiar nullable según tu diseño
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> subcategories;

    public Category(String name, String image, Category parent) {
        this.name = name;
        this.image = image;
        this.parentCategory = parentCategory;
    }
}
