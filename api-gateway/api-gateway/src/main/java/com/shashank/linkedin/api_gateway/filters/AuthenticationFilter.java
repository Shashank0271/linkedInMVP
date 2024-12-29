package com.shashank.linkedin.api_gateway.filters;

import com.shashank.linkedin.api_gateway.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.debug("Logging info : {}", exchange.getRequest()
                                                   .getURI());

            final String tokenHeader =
                    exchange.getRequest().getHeaders().getFirst(
                            "Authorization");
            if (tokenHeader == null || !tokenHeader.startsWith(
                    "Bearer ")) {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            final String token = tokenHeader.split("Bearer ")[1];

            String userID = jwtService.getUserIdFromToken(token);

            return chain.filter(exchange.mutate()
                                        .request(r -> r.header("X-User-Id", userID))
                                        .build());
        };
    }

    public static class Config {
    }
}
