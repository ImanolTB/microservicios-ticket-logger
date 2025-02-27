package org.iesalixar.daw2.ImanolTrespaderne.dwese_ticket_logger_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSocketSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain websocketSecurityFilterChain(HttpSecurity http)throws Exception{
        http
                .securityMatcher("/ws/**") //Aplicar esta configuración solo a las rutas que empiecen por /ws/
                .authorizeHttpRequests(auth ->auth
                        .anyRequest().permitAll()) //Permitir accesp sin autenticacion a los webSockets
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))//Permitir websockets en navegadores sin restricciones
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
