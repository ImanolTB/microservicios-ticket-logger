package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Filtro para extraer la autenticación desde los headers enviados por el API Gateway.
 *
 * Este filtro se encarga de obtener el usuario autenticado y sus roles
 * desde las cabeceras HTTP ("X-Authenticated-User" y "X-Authenticated-Roles"),
 * estableciendo la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    /**
     * Método que intercepta cada solicitud HTTP y extrae la autenticación desde los headers.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        // Extraer usuario y roles desde los headers enviados por el API Gateway
        String username = request.getHeader("X-Authenticated-User");
        String rolesHeader = request.getHeader("X-Authenticated-Roles");


        if (username != null && rolesHeader != null) {
            // Convertir los roles de String a objetos SimpleGrantedAuthority
            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());


            // Crear objeto de autenticación para Spring Security
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);


            // Establecer la autenticación en el contexto de seguridad de Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);


            logger.info("✅ Usuario autenticado desde los headers: {}", username);
        } else {
            logger.warn("⚠️ No se encontró información de autenticación en los headers.");
        }


        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
