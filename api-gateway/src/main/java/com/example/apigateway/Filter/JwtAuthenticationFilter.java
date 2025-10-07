package com.example.apigateway.Filter;

import com.example.apigateway.Util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.NameConfig> {

    private final JwtUtil jwtUtil;

    private final ObjectMapper objectMapper=new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil){
        super(NameConfig.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(NameConfig config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request=  exchange.getRequest();

            String authHeader=request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token=null;

            if(authHeader != null && authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);
            }

            if(token == null){
                return haddleUnotherized(exchange, "Authorization token not provided");
            }

            if(!jwtUtil.validateToken(token)){
                return haddleUnotherized(exchange, "Invalid or expired token");
            }
            try{
                String username = jwtUtil.extractEmail(token);
                String userId = jwtUtil.extractId(token);
                String role = jwtUtil.extractRole(token);

                // Add user information to request headers for downstream services
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-Username", username)
                        .header("X-User-Role", role)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            }catch (Exception e){
                return haddleUnotherized(exchange, "Token processing error");
            }
        });
    }

    private Mono<Void> haddleUnotherized(ServerWebExchange exchange,String message){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", message);
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            DataBuffer buffer = response.bufferFactory().wrap("Unauthorized".getBytes());
            return response.writeWith(Mono.just(buffer));
        }
    }
}
