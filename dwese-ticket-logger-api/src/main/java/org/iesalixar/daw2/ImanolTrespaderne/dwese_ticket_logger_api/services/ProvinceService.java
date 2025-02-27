package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers.RegionController;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Province;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Region;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers.ProvinceMapper;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


    @Service
    public class ProvinceService {
        private static final Logger logger = LoggerFactory.getLogger(RegionController.class);


        // DAO para gestionar las operaciones de las regiones en la base de datos
        @Autowired
        private ProvinceRepository provinceRepository;
        @Autowired
        private ProvinceMapper provinceMapper;
        @Autowired
        private MessageSource messageSource;
        /**
         * Lista las provicnias almacenadas en la base de datos
         .
         * @return ResponseEntity con la lista de provincias o un error en caso de fallo.
         */
        @GetMapping
        public Page<ProvinceDTO> getAllProvinces(Pageable pageable) {
            logger.info("Solicitando la lista de todas las regiones...");
            try {
                Page<Province> provinces = provinceRepository.findAll(pageable);
                logger.info("Se han cargado {} regiones.", provinces.getTotalElements());
                return provinces
                        .map(provinceMapper::toDTO)
                        ;
            } catch (Exception e){
                logger.error("Error al listar las provincias: {}", e.getMessage());
                throw new RuntimeException("Error al obtener todas las provincias");
            }



        }


        /**
         * Obtiene una provincia especifica por su id
         *
         * @param id ID de la provincia solicitada
         * @return ResponseEntity con la provincia encontrada o un mensaje de error si no existe.
         */
        @GetMapping("/{id}")
        public Optional<ProvinceDTO> getProvinceById(@PathVariable Long id) {
            logger.info("Buscando provincia con id: {}", id);
            try {
                return provinceRepository.findById(id).map(provinceMapper::toDTO);
            } catch (Exception e) {
                logger.warn("Error al buscar la provincia con id: {}", id);
                throw new RuntimeException("Error al obtener la provincia con id: "+id);
            }
        }



        /**
         * Crea una nueva provincia en la base de datos.
         *
         * @param provinceDTO  Objeto JSON que representa a la nueva provincia
         * @param locale  Idioma de los mensajes de error.
         * @return ResponseEntity con la provincia creada o un mensaje de error.
         */
        @PostMapping
        public ProvinceDTO createProvince(@Valid @RequestBody ProvinceDTO provinceDTO, Locale locale) {
            logger.info("Creando nueva provincia con código {}", provinceDTO.getCode());
            if (provinceRepository.existsProvinceByCode(provinceDTO.getCode())) {
                String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
                logger.warn("Error al crear provincia : {}", errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            Province province = provinceMapper.toEntity(provinceDTO);
            Province savedProvince = provinceRepository.save(province);
            logger.info("Region creada exitosamente con id {}", savedProvince.getId());
            return provinceMapper.toDTO(savedProvince);


        }

        /**
         * Actualiza una provincia existente en la base de datos.
         *
         * @param id  ID de la provincia a actualizar
         * @param provinceDTO  Objeto JSON con los nuevos datos
         * @param  locale Idioma de los mensajes de error
         * @return ResponseEntity con la provincia actualizada o un mensaje de error.
         */
        @PutMapping("/{id}")
        public ProvinceDTO updateProvince(@PathVariable Long id, @Valid @RequestBody ProvinceDTO provinceDTO, Locale locale) {
            logger.info("Actualizando provincia con ID {}", provinceDTO.getId());

            Province existingProvince= provinceRepository.findById(id)
                    .orElseThrow(()->new IllegalArgumentException("La provincia no existe."));

            if(provinceRepository.existsProvinceByCodeAndNotId(provinceDTO.getCode(), id)){
                String errorMessage= messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
                logger.warn("Error al actualizar provincia : {}", errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

            existingProvince.setCode(provinceDTO.getCode());
            existingProvince.setName(provinceDTO.getName());
            Province updatedProvince= provinceRepository.save(existingProvince);
            logger.info("Región con id {} actualizada exitosamente  ", id);
            return provinceMapper.toDTO(updatedProvince);

        }



        /**
         * Elimina una provincia de la base de datos.
         *
         * @param id     ID de la provincia a eliminar.
         * @return ResponseEntity indicando el resultado de la operación.
         */
        @DeleteMapping ("/{id}")
        public void  deleteProvince(@PathVariable Long id) {
            logger.info("Eliminando provincia con ID {}", id);

            if(!provinceRepository.existsById(id)){
                logger.warn("No se encontró ninguna provincia con id : {}", id);
                throw new IllegalArgumentException("La provincia no existe.");
            }
            provinceRepository.deleteById(id);
            logger.info("Provincia con ID {} eliminada con éxito.", id);

        }
}
