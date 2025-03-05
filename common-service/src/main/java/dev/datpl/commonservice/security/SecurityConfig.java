package dev.datpl.commonservice.security;

import dev.datpl.commonservice.config.ConfigProperties;
import dev.datpl.commonservice.security.filter.RequestIDFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final ConfigProperties configProperties;
    private final RequestIDFilter requestIDFilter;

//    Spring Security’s hasRole("XYZ") automatically adds "ROLE_" as a prefix when checking authorities. So:
//
//    If your authority is "role_partner", Spring Security won't match it with hasRole("role_partner") because it's looking for "ROLE_role_partner".
//    By updating the prefix to "ROLE_", your roles will match properly.

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/api/common/health").permitAll()
                        .pathMatchers("/api/common/users/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/api/training/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINER", "ROLE_TRAINEE", "ROLE_PARTNER")
                        .pathMatchers("/api/training/health").permitAll()
                        .pathMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/api/admin/health").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(requestIDFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, exception) ->
                                Mono.fromRunnable(() ->
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                                )
                        )
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(configProperties.getAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                log.warn("No 'realm_access.roles' found in JWT: {}", jwt.getClaims());
                return Flux.empty();
            }
            List<String> roles = (List<String>) realmAccess.get("roles");

            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())) // Prefix with "ROLE_"
                    .collect(Collectors.toList());
            return Flux.fromIterable(authorities);
        });

        return jwtAuthenticationConverter;
    }

}
