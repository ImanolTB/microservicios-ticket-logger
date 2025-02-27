package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.CategoryCreateDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.CategoryDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Category;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.CategoryRepository;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.CategoryService;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.FileStorageService;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ticket-logger/categories")
public class CategoryController {


    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);


    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private CategoryService categoryService;
    /**
     * Lista las regiones almacenadas en la base de datos
     .
     * @return ResponseEntity con la lista de regiones o un error en caso de fallo.
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("Solicitando la lista de todas las categorias...");
        try {

            List<CategoryDTO>categories=categoryService.getAllCategories();
            logger.info("Se han cargado {} categorias.", categories.size());
            return ResponseEntity.ok(categories);
        } catch (Exception e){
            logger.error("Error al listar las categorias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }



    }


    /**
     * Obtiene nua region especifica por su id
     *
     * @param id ID de la region solicitada
     * @return ResponseEntity con la region encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        logger.info("Buscando categoria con id: {}", id);
        try {
            Optional<CategoryDTO> categoryDTO = categoryService.getCategoryById(id);
            if (categoryDTO.isPresent()) {
                logger.info("Categoria con id {} encontrada: {}", id, categoryDTO.get());
                return ResponseEntity.ok(categoryDTO.get());
            }
            else {
                logger.warn("No se encontró ninguna region con id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna region con id: "+id);
            }
        } catch (Exception e) {
            logger.warn("Error al buscar la region con id: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    /**
     * Crea una nueva región en la base de datos.
     *
     * @param createDTO  Objeto JSON que representa a la nueva categoria
     * @param locale  Idioma de los mensajes de error.
     * @return ResponseEntity con la categoria creada o un mensaje de error.
     */
    @PostMapping (consumes = "multipart/form-data")
    public ResponseEntity <?> createCategory(@Valid @ModelAttribute CategoryCreateDTO createDTO, Locale locale) {
        try {
            CategoryDTO categoryDTO=categoryService.createCategory(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (RuntimeException e){
            logger.error("Error al guardar la imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
        }
        catch (Exception e) {
            logger.error("Error al crear la region: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la categoría");
        }
    }


    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param id  ID de la region a actualizar
     * @param updateDTO  Objeto JSON con los nuevos datos
     * @param  locale Idioma de los mensajes de error
     * @return ResponseEntity con la categoria actualizada o un mensaje de error.
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity <?> updateCategory(@PathVariable Long id, @Valid @ModelAttribute CategoryCreateDTO updateDTO, Locale locale) {
        logger.info("Actualizando categoría con ID {}", id);
        try {
            CategoryDTO updatedCategory=categoryService.updateCategory(id, updateDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error al guardar la imagen  para la categoría con ID {} : {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
        }
        catch (Exception e) {
            logger.error("Error al actualizar la categoría: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la categoría");
        }
    }



    /**
     * Elimina una región de la base de datos.
     *
     * @param id     ID de la región a eliminar.
     * @return ResponseEntity indicandoel resultado de la operación.
     */
    @DeleteMapping ("/{id}")
    public ResponseEntity<?>  deleteCategory(@PathVariable Long id) {
        logger.info("Eliminando categoría con ID {}", id);
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Categoría eliminada con éxito");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error al eliminar la categoría con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoría");
        }
    }
}
