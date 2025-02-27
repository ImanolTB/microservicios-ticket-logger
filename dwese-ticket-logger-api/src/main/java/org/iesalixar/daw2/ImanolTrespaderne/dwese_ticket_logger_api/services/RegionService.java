package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers.RegionController;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Region;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class RegionService {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);


    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private MessageSource messageSource;
    /**
     * Lista las regiones almacenadas en la base de datos
     .
     * @return ResponseEntity con la lista de regiones o un error en caso de fallo.
     */
    @GetMapping
    public Page<RegionDTO> getAllRegions(Pageable pageable) {
        logger.info("Solicitando la lista de todas las regiones...");
        try {
            Page<Region> regions = regionRepository.findAll(pageable);
            logger.info("Se han cargado {} regiones.", regions.getNumberOfElements());
            return regions.map(regionMapper::toDTO)
                   ;
        } catch (Exception e){
            logger.error("Error al listar las regiones: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las regiones");
        }



    }


    /**
     * Obtiene nua region especifica por su id
     *
     * @param id ID de la region solicitada
     * @return ResponseEntity con la region encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public Optional<RegionDTO> getRegionById(@PathVariable Long id) {
        logger.info("Buscando region con id: {}", id);
        try {
                return regionRepository.findById(id).map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.warn("Error al buscar la region con id: {}", id);
            throw new RuntimeException("Error al obtener la region con id: "+id);
        }
    }



    /**
     * Crea una nueva región en la base de datos.
     *
     * @param regionDTO  Objeto JSON que representa a la nueva region
     * @param locale  Idioma de los mensajes de error.
     * @return ResponseEntity con la region creada o un mensaje de error.
     */
    @PostMapping
    public RegionDTO createRegion(@Valid @RequestBody RegionDTO regionDTO, Locale locale) {
        logger.info("Creando nueva región con código {}", regionDTO.getCode());
        if (regionRepository.existsRegionByCode(regionDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            logger.warn("Error al crear region : {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        Region region = regionMapper.toEntity(regionDTO);
        Region savedRegion = regionRepository.save(region);
        logger.info("Region creada exitosamente con id {}", savedRegion.getId());
        return regionMapper.toDTO(savedRegion);


    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param id  ID de la region a actualizar
     * @param regionDTO  Objeto JSON con los nuevos datos
     * @param  locale Idioma de los mensajes de error
     * @return ResponseEntity con la region actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public RegionDTO updateRegion(@PathVariable Long id, @Valid @RequestBody RegionDTO regionDTO, Locale locale) {
        logger.info("Actualizando región con ID {}", regionDTO.getId());

            Region existingRegion= regionRepository.findById(id)
                    .orElseThrow(()->new IllegalArgumentException("La region no existe."));

            if(regionRepository.existsRegionByCodeAndNotId(regionDTO.getCode(), id)){
                String errorMessage= messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
                logger.warn("Error al actualizar region : {}", errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

            existingRegion.setCode(regionDTO.getCode());
            existingRegion.setName(regionDTO.getName());
            Region updatedRegion= regionRepository.save(existingRegion);
            logger.info("Región con id {} actualizada exitosamente  ", id);
            return regionMapper.toDTO(updatedRegion);

    }



    /**
     * Elimina una región de la base de datos.
     *
     * @param id     ID de la región a eliminar.
     * @return ResponseEntity indicandoel resultado de la operación.
     */
    @DeleteMapping ("/{id}")
    public void  deleteRegion(@PathVariable Long id) {
        logger.info("Eliminando región con ID {}", id);

            if(!regionRepository.existsById(id)){
                logger.warn("No se encontró ninguna region con id : {}", id);
                throw new IllegalArgumentException("La region no existe.");
            }
            regionRepository.deleteById(id);
            logger.info("Región con ID {} eliminada con éxito.", id);

    }
}



