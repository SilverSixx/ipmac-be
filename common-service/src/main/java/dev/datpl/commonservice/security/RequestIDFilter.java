package dev.datpl.commonservice.security;

import dev.datpl.commonservice.config.Oauth2ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestIDFilter implements WebFilter {

    private final Oauth2ConfigProperties oauth2ConfigProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (exchange.getRequest().getPath().toString().contains("/api/common/health")) {
            return chain.filter(exchange);
        }

        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("X-Requested-With"))
                .filter(requestId -> oauth2ConfigProperties.getClients().contains(requestId))
                .flatMap(valid -> chain.filter(exchange))
                .switchIfEmpty(Mono.defer(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }));
    }

}
