package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 1) Seguridad para WebSockets
    @Bean
    @Order(1)
    SecurityFilterChain websocketChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/ws/**")     // ¡solo WS!
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()  // o .authenticated() si lo validas en el interceptor STOMP
                );
        return http.build();
    }

    // 2) Seguridad para la API
    @Bean
    @Order(2)
    SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // opcional
                        .requestMatchers("/api/v1/auth/**", "/api/v1/register", "/ws/**").permitAll()

                        // Reglas específicas (ajústalas a tu política)
                        .requestMatchers("/api/v1/ticket-logger/regions").hasAnyRole("USER","MANAGER","ADMIN")
                        .requestMatchers("/api/v1/ticket-logger/provinces",
                                "/api/v1/ticket-logger/supermarkets",
                                "/api/v1/ticket-logger/locations",
                                "/api/v1/ticket-logger/categories").hasAnyRole("MANAGER","ADMIN")
                        .requestMatchers("/api/v1/ticket-logger/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}