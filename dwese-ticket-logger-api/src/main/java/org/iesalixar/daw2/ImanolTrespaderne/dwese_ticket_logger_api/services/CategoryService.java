package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers.RegionController;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.CategoryCreateDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.CategoryDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Category;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Region;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers.CategoryMapper;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.CategoryRepository;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);


    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private MessageSource messageSource;
    /**
     * Lista las regiones almacenadas en la base de datos
     .
     * @return ResponseEntity con la lista de regiones o un error en caso de fallo.
     */
    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        logger.info("Solicitando la lista de todas las categorias...");
        try {
            List<Category> categories = categoryRepository.findAll();
            logger.info("Se han cargado {} regiones.", categories.size());
            return categories.stream()
                    .map(categoryMapper::toDTO)
                    .toList();
        } catch (Exception e){
            logger.error("Error al listar las categorias: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las categorias");
        }



    }


    /**
     * Obtiene una categoria especifica por su id
     *
     * @param id ID de la region solicitada
     * @return ResponseEntity con la region encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public Optional<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.info("Buscando categoria con id: {}", id);
        try {
            return categoryRepository.findById(id).map(categoryMapper::toDTO);
        } catch (Exception e) {
            logger.warn("Error al buscar la categoria con id: {}", id);
            throw new RuntimeException("Error al obtener la categoria con id: "+id);
        }
    }



    /**
     * Crea una nueva categoria en la base de datos.
     *
     * @param createDTO  Objeto JSON que representa a la nueva categoria
     * @return ResponseEntity con la categoría creada o un mensaje de error.
     */
    @PostMapping
    public CategoryDTO createCategory(@Valid @RequestBody CategoryCreateDTO createDTO) {
        logger.info("Creando nueva categoría con código {}", createDTO.getName());
        if (categoryRepository.existsCategoryByName(createDTO.getName())) {
            throw new IllegalArgumentException("El nombre de la categoría ya existe");
        }
        //Obtener la categoria padre si está presente
        Category parentCategory =null;
        if(createDTO.getParentCategoryId() != null){
            parentCategory= categoryRepository.findById(createDTO.getParentCategoryId())
                    .orElse(null);
        }
        //Procesar la imagen si se proporiona
        String fileName= null;
        if(createDTO.getImageFile() != null && !createDTO.getImageFile().isEmpty()){
            fileName=fileStorageService.saveFile(createDTO.getImageFile());
        }
        if(fileName==null){
            throw  new RuntimeException("Error al guardar la imagen");
        }
        //Crear la entidad Category
        Category category = categoryMapper.toEntity(createDTO, parentCategory);
        category.setImage(fileName);
//Guardar la nueva categoría
        Category savedCategory=categoryRepository.save(category);
        logger.info("Categoria creada exitosamente con id {}", savedCategory.getId());
        return categoryMapper.toDTO(savedCategory);


    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param id  ID de la region a actualizar
     * @param updateDTO  Objeto JSON con los nuevos datos
     * @return ResponseEntity con la region actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateDTO updateDTO) {
        logger.info("Actualizando categoria con ID {}", id);
//Buscar la categoría existente
        Category existingCategory= categoryRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("La categoría no existe."));
//Verificar si el nombre ya está en uso por otra categoría
        if(categoryRepository.existsCategoryByNameAndNotId(updateDTO.getName(), id)){
            throw new IllegalArgumentException("El nombre de la categoría ya está en uso.");
        }
        //Obtener categoría padre si está presente
        Category parentCategory= null;
        if(updateDTO.getParentCategoryId() !=null){
            parentCategory=categoryRepository.findById(updateDTO.getParentCategoryId())
                    .orElse(null);
        }
        //Procesar la imagen si se proporciona.
String fileName= existingCategory.getImage(); //Conservar la imagen existente por defecto.
        if(updateDTO.getImageFile() != null && !updateDTO.getImageFile().isEmpty()){
            fileName=fileStorageService.saveFile(updateDTO.getImageFile());
            if(fileName==null){
                throw new RuntimeException("Error al guardar la nueva imagen.");
            }
        }
        //ACatualizar los datos de la categoria
        existingCategory.setName(updateDTO.getName());
        existingCategory.setParentCategory(parentCategory);
        existingCategory.setImage(fileName);
        Category updatedCategory= categoryRepository.save(existingCategory);
        logger.info("Categoría con id {} actualizada exitosamente  ", id);
        return categoryMapper.toDTO(updatedCategory);

    }



    /**
     * Elimina una región de la base de datos.
     *
     * @param id     ID de la región a eliminar.
     * @return ResponseEntity indicandoel resultado de la operación.
     */
    @DeleteMapping ("/{id}")
    public void  deleteCategory(@PathVariable Long id) {
        logger.info("Eliminando categoría con ID {}", id);
Category category= categoryRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("La categoría no existe"));
if(category.getImage() != null && category.getImage().isEmpty()){
    fileStorageService.deleteFile(category.getImage());
    logger.info("Imagen asociada a la categoría  con ID {} eliminada", id);
}
        categoryRepository.deleteById(id);
        logger.info("Categoría con ID {} eliminada con éxito.", id);

    }
}
