package com.Ashish.api_gateway.filters;

import com.Ashish.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


// If we name it like that then we can directly use the filter name "Authentication" because spring will automatically remove the rest of the part.
@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    @Data
    public static class Config{
        private boolean enabled;
    }

    public AuthenticationGatewayFilterFactory(JwtService jwtService){
        super(Config.class);
        this.jwtService = jwtService;
    }

    // In the authentication part, we are not writing the code to generate the token. We are simply taking token from the user and verifying that. How user will get the token, we can generate a new token from the jwt.io website using our secret key.
    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain) -> {

            if(!config.isEnabled()){ // means Authentication is disabled → skip this authentication filter and continue the request normally.
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if(null == authHeader){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete(); // Stop processing this request and complete the HTTP response immediately.
            }

            String token = authHeader.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);

            // See we didn't make changes in the original exchange here. We just get a new ServerHttpRequest.
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                                                        .mutate() // using this we can update/mutate the request.
                                                        .header("X-User-Id", userId.toString())
                                                        .build();



            // Now were are actually mutating the whole exchange.
            ServerWebExchange newRequest = exchange.mutate()
                                                    .request(mutatedRequest)
                                                    .build();
            return chain.filter(newRequest);
        };
    }
}
