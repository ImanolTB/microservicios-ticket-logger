package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Ticket Logger ", version = "1.0", description = "API para gestión de tickets realizada por Imanol Trespaderne"),
        security = @SecurityRequirement(name = "bearerAuth") // Aplica a toda la API
)
@SecurityScheme(
        name = "bearerAuth", // Este nombre se usará en @SecurityRequirement
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class OpenAPIConfig {
}
