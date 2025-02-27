package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.ProvinceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Province`.
 */
@RestController
@RequestMapping("/api/v1/ticket-logger/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    @Operation(summary = "Lista todas las provincias")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de provincias obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<ProvinceDTO>> getAllProvinces(@PageableDefault(size = 10,sort = "name") Pageable pageable) {
        logger.info("Solicitando la lista de todas las provincias...");
        try {
            Page<ProvinceDTO> provinceDTOs = provinceService.getAllProvinces(pageable);
            return ResponseEntity.ok(provinceDTOs);
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtiene una provincia por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provincia encontrada"),
            @ApiResponse(responseCode = "404", description = "Provincia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProvinceById(@PathVariable Long id) {
        logger.info("Buscando region con id: {}", id);
        try {
            Optional<ProvinceDTO> provinceDTO = provinceService.getProvinceById(id);
            if (provinceDTO.isPresent()) {
                logger.info("Region con id {} encontrada: {}", id, provinceDTO.get());
                return ResponseEntity.ok(provinceDTO.get());
            }
            else {
                logger.warn("No se encontró ninguna provincia con id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna provincia con id: "+id);
            }
        } catch (Exception e) {
            logger.warn("Error al buscar la provincia con id: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @Operation(summary = "Crea una nueva provincia")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Provincia creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> createProvince(@Valid @RequestBody ProvinceDTO provinceDTO, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", provinceDTO.getCode());
        try {
            ProvinceDTO createdProvince = provinceService.createProvince(provinceDTO, locale);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProvince);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la provincia");
        }
    }

    @Operation(summary = "Actualiza una provincia existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provincia actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvince(@PathVariable Long id, @Valid @RequestBody ProvinceDTO provinceDTO, Locale locale) {
        logger.info("Actualizando provincia con ID {}", provinceDTO.getId());
        try {
            ProvinceDTO updatedProvince = provinceService.updateProvince(id, provinceDTO, locale);
            return ResponseEntity.ok(updatedProvince);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la provincia");
        }
    }

    @Operation(summary = "Elimina una provincia por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provincia eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        logger.info("Eliminando provincia con ID {}", id);
        try {
            provinceService.deleteProvince(id);
            return ResponseEntity.ok("Provincia eliminada con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la provincia");
        }
    }
}
