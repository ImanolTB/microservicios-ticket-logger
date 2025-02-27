package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/ticket-logger/images")
public class FileStorageController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @GetMapping ("/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName){
        try {
            //Construir la ruta completa del archivo a partir del directorio de lmacenamiento y el nombre del archivo.
            Path filePath= Paths.get(uploadPath).resolve(fileName).normalize();
            //Crear un recurso a partir de la URI  del archivo.
            Resource resource= new UrlResource(filePath.toUri());

            if(resource.exists() && resource.isReadable()){
                logger.info("Sirviendo archivo : {}", fileName);
                //intenta detectar el tipo MIME del archivo.
                String contenType= Files.probeContentType(filePath);
                if(contenType == null){
                    //Si no se puede determinar el tipo de MIME, se asigna un tipo genérico.
                    contenType= "application/octet-stream";
                    logger.warn("No se pudo detectar el MIME del archivo {}. Se usará el tipo genérico", fileName);
                }
                //Devolver el archivo con el MIME y configuración adecuada para mostrarlo en linea.
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contenType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                //si el archivo no existe o no es accesible, devolver un error 404 (not found)
                logger.error("El archivo {} no existe o no se puede leer", fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El archivo "+fileName+" no existe o no se puede leer");
            }
        } catch (IOException e) {
            logger.error("Error al servir el archivo {} : {}",fileName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
