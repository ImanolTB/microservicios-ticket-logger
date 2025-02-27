package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.entity.Supermarket;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/ticket-logger/supermarkets")
public class SupermarketController {
    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);


    // DAO para gestionar las operaciones de los supermercados en la base de datos
    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private MessageSource messageSource;
    /**
     * Lista todos los supermercados y las pasa como atributo al modelo para que sean
     * accesibles en la vista `supermarket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de regiones.
     */
    @GetMapping
    public String listSupermarkets(Model model) {
        logger.info("Solicitando la lista de todos los supermercados...");
        List<Supermarket> listSupermarkets = supermarketRepository.findAll();
        logger.info("Se han cargado {} supermercados.", listSupermarkets.size());
        model.addAttribute("listSupermarkets", listSupermarkets); // Pasar la lista de supermercados al modelo
        return "supermarket"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear un nuevo supermercado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo supermercado.");
        model.addAttribute("supermarket", new Supermarket()); // Crear un nuevo objeto supermarket
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar un supermercado existente.
     *
     * @param id    ID del supermercado a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el supermercado con ID {}", id);
      Optional<Supermarket> supermarket = supermarketRepository.findById(id);
        if (supermarket.isEmpty()) {
            logger.warn("No se encontró el supermercado con ID {}", id);
        }
        model.addAttribute("supermarket", supermarket.get());
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta un nuevo supermercado en la base de datos.
     *
     * @param supermarket              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/insert")
    public String insertSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo supermercado con nombre {}", supermarket.getName());
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (supermarketRepository.existsSupermarketByName(supermarket.getName())) {
            logger.warn("El nombre del supermercado {} ya existe.", supermarket.getName());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/supermarkets/new";
        }
        supermarketRepository.save(supermarket);
        logger.info("Supermercado {} insertado con éxito.", supermarket.getName());
        return "redirect:/supermarkets"; // Redirigir a la lista de regiones
    }


    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param supermarket              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/update")
    public String updateSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando supermercado con ID {}", supermarket.getId());
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (supermarketRepository.existsSupermarketByNameAndNotId(supermarket.getName(), supermarket.getId())) {
            logger.warn("El nombre del supermerado {} ya existe para otro supermercado.", supermarket.getName());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/supermarkets/edit?id=" + supermarket.getId();
        }
        supermarketRepository.save(supermarket);
        logger.info("supermercado con ID {} actualizado con éxito.", supermarket.getId());
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }



    /**
     * Elimina una región de la base de datos.
     *
     * @param id                 ID de la región a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/delete")
    public String deleteSupermarket(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando supermercado con ID {}", id);
        try {
            supermarketRepository.deleteById(id);
            logger.info("supermercado con ID {} eliminado con éxito.", id);
        }
        catch (DataIntegrityViolationException d) {
            logger.error("Error al eliminar el supermercado con ID {}: {}", id, d.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el supermercado.");
        }
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }
}
