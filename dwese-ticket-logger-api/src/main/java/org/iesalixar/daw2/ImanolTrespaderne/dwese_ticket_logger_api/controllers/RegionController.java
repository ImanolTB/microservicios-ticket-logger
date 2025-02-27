package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.RegionDTO;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Controlador que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@RestController
@RequestMapping("/api/v1/ticket-logger/regions")
public class RegionController {


    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);


    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private RegionService regionService;
    /**
     * Lista las regiones almacenadas en la base de datos
     .
     * @return ResponseEntity con la lista de regiones o un error en caso de fallo.
     */

        @Operation(summary = "Obtener todas las regiones", description = "Devuelve una lista de todas las regiones registradas en el sistema.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de regiones obtenida exitosamente"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener las regiones")
        })
        @GetMapping
        public ResponseEntity<Page<RegionDTO>> getAllRegions(@PageableDefault(size = 10,sort = "name") Pageable pageable) {
            logger.info("Solicitando la lista de todas las regiones...");
            try {
                Page<RegionDTO> regionDTOs = regionService.getAllRegions(pageable);
                logger.info("Se han cargado {} regiones.", regionDTOs.getTotalElements());
                return ResponseEntity.ok(regionDTOs);
            } catch (Exception e) {
                logger.error("Error al listar las regiones: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @Operation(summary = "Obtener una región por ID", description = "Devuelve una región específica según su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Región encontrada exitosamente"),
                @ApiResponse(responseCode = "404", description = "No se encontró ninguna región con el ID proporcionado"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{id}")
        public ResponseEntity<?> getRegionById(@PathVariable Long id) {
            logger.info("Buscando region con id: {}", id);
            try {
                Optional<RegionDTO> regionDTO = regionService.getRegionById(id);
                if (regionDTO.isPresent()) {
                    logger.info("Region con id {} encontrada: {}", id, regionDTO.get());
                    return ResponseEntity.ok(regionDTO.get());
                }
                else {
                    logger.warn("No se encontró ninguna region con id: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna region con id:" + id);
                }
            } catch (Exception e) {
                logger.warn("Error al buscar la region con id: {}", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @Operation(summary = "Crear una nueva región", description = "Crea una nueva región en el sistema.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Región creada exitosamente"),
                @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos incorrectos"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PostMapping
        public ResponseEntity<?> createRegion(@Valid @RequestBody RegionDTO regionDTO, Locale locale) {
            logger.info("Insertando nueva región con código {}", regionDTO.getCode());
            try {
                RegionDTO createdRegion = regionService.createRegion(regionDTO, locale);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                logger.error("Error al crear la región: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la región");
            }
        }

        @Operation(summary = "Actualizar una región", description = "Actualiza una región existente según su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Región actualizada exitosamente"),
                @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos incorrectos"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PutMapping("/{id}")
        public ResponseEntity<?> updateRegion(@PathVariable Long id, @Valid @RequestBody RegionDTO regionDTO, Locale locale) {
            logger.info("Actualizando región con ID {}", id);
            try {
                RegionDTO updatedRegion = regionService.updateRegion(id, regionDTO, locale);
                return ResponseEntity.ok(updatedRegion);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                logger.error("Error al actualizar la región: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la región");
            }
        }

        @Operation(summary = "Eliminar una región", description = "Elimina una región según su ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Región eliminada exitosamente"),
                @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
            logger.info("Eliminando región con ID {}", id);
            try {
                regionService.deleteRegion(id);
                return ResponseEntity.ok("Región eliminada con éxito");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la región");
            }
        }
    }

