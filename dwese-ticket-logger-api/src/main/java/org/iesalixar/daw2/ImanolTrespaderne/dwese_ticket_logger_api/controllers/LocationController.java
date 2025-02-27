package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Location;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Province;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Supermarket;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.LocationRepository;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/ticket-logger/locations")
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);


    // DAO para gestionar las operaciones de las provincias y las regiones en la base de datos
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private SupermarketRepository supermarketRepository;
    @Autowired
    private MessageSource messageSource;
    /**
     * Lista todas las localizaciones y las pasa como atributo al modelo para que sean
     * accesibles en la vista `location.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de provincias.
     */
    @GetMapping
    public String listLocations(Model model) {
        logger.info("Solicitando la lista de todas las localizaciones...");
        List<Location> listLocations = locationRepository.findAll();
        logger.info("Se han cargado {} localizaciones.", listLocations.size());
        model.addAttribute("listLocations", listLocations); // Pasar la lista de provincias al modelo
        return "location"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear una nueva localizacion.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva localizacion.");
        List<Province> listProvinces= provinceRepository.findAll();
        List<Supermarket> listSupermarkets= supermarketRepository.findAll();

        logger.info("Se han cargado {} provincias.", listProvinces.size());

        logger.info("Se han cargado {} supermercados.", listSupermarkets.size());


        model.addAttribute("listSupermarkets", listSupermarkets);
        model.addAttribute("listProvinces", listProvinces);
        model.addAttribute("location", new Location()); // Crear un nuevo objeto Provincia
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**p
     * Muestra el formulario para editar una provincia existente.
     *
     * @param id    ID de la localizacion a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Optional <Location> location=locationRepository.findById(id);
        List<Province> listProvinces = provinceRepository.findAll();
        List<Supermarket> listSupermarkets=supermarketRepository.findAll();

        logger.info("Se han cargado {} provincias.", listProvinces.size());

        logger.info("Se han cargado {} supermercados.", listSupermarkets.size());

        if (location.isEmpty()) {
            logger.warn("No se encontró la localizacion con ID {}", id);
        }
        model.addAttribute("listProvinces", listProvinces); // Pasar la lista de provincias al modelo
        model.addAttribute("listSupermarkets", listSupermarkets); // Pasar la lista de supermercados al modelo
        model.addAttribute("location", location.get());
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva localización en la base de datos.
     *
     * @param location Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/insert")
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Insertando nueva localizacion con direccion {} y ciudad {}", location.getAddress(), location.getCity());
        if (result.hasErrors()) {
            List<Province> listProvinces = provinceRepository.findAll();
            logger.info("Se han cargado {} provincias.", listProvinces.size());
            model.addAttribute("listProvinces", listProvinces); // Pasar la lista de provincias al modelo
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (result.hasErrors()){
            List<Supermarket> listSupermarkets= supermarketRepository.findAll();
            logger.info("Se han cargado {} supermercados.", listSupermarkets.size());
            model.addAttribute("listSupermarkets", listSupermarkets); // Pasar la lista de provincias al modelo
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (locationRepository.existsLocationByAddressAndCity(location.getAddress(), location.getCity())) {
            logger.warn("La direccion de la localizacion {} ya existe.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.insert.addressExists", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/new";
        }

        locationRepository.save(location);
        logger.info("localizacion {} insertada con éxito.", location.getAddress());
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }


    /**
     * Actualiza una localizacion existente en la base de datos.
     *
     * @param location              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/update")
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Actualizando localizacion con ID {}", location.getId());
        if (result.hasErrors()) {
            List<Province> listProvinces = provinceRepository.findAll();
            logger.info("Se han cargado {} provincias.", listProvinces.size());
            model.addAttribute("listProvinces", listProvinces); // Pasar la lista de provincias al modelo
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (result.hasErrors()){
            List<Supermarket> listSupermarkets=supermarketRepository.findAll();
            logger.info("Se han cargado {} supermercados.", listSupermarkets.size());
            model.addAttribute("listSupermarkets", listSupermarkets); // Pasar la lista de provincias al modelo
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (locationRepository.existsLocationByAddressAndNotId(location.getAddress(), location.getId())) {
            logger.warn("La direccion de la localizacion {} ya existe para otra localizacion.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.update.addressExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/edit?id=" + location.getId();
        }
        if (locationRepository.existsLocationByCityAndNotId(location.getCity(), location.getId())) {
            logger.warn("La ciudad de la localizacion {} ya existe para otra localizacion.", location.getCity());
            String errorMessage = messageSource.getMessage("msg.location-controller.update.cityExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/edit?id=" + location.getId();
        }
        locationRepository.save(location);
        logger.info("Localizacion con ID {} actualizada con éxito.", location.getId());
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }



    /**
     * Elimina una localizacion de la base de datos.
     *
     * @param id                 ID de la provincia a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando localizacion con ID {}", id);
        locationRepository.deleteById(id);
        logger.info("Ubicacion con ID {} eliminada con éxito.", id);
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }
}
