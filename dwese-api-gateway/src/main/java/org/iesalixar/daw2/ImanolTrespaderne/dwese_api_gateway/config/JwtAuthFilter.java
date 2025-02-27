package org.iesalixar.daw2.ImanolTrespaderne.dwese_api_gateway.config;

import io.jsonwebtoken.Claims;
import org.iesalixar.daw2.ImanolTrespaderne.dwese_api_gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of(
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register"
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request=exchange.getRequest();

        if(PUBLIC_URLS.stream().anyMatch(url-> request.getURI().getPath().startsWith(url))){
            return chain.filter(exchange);
        }
        String authHeader= request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return unauthorized(exchange, "Token no encontrado o mal formado");
        }
        String token = authHeader.substring(7);

        String username;
        try {
            username=jwtUtil.extractUsername(token);
        }catch (Exception e){
            return unauthorized(exchange, "Token invalido: "+ e.getMessage());
        }

        if(!jwtUtil.validateToken(token, username)){
            return unauthorized(exchange, "Token expirado o inválido");
        }

        Claims claims = jwtUtil.extractAllClaims(token);

        List<String> roles= claims.get("roles", List.class);

        ServerHttpRequest modifiedRequest= exchange.getRequest().mutate()
                .header("X-Authenticated-User", username)
                .header("X-Authenticated-Roles", String.join(",", roles))
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String mensaje){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
