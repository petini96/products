package br.com.roboticsmind.products.filters.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        // --- Endpoints Públicos (sem autenticação) ---
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers("/api/users/me/status").permitAll()
                        .requestMatchers("/public/**").permitAll()

                        // --- Endpoints Protegidos por Papel ---
                        .requestMatchers(HttpMethod.POST, "/api/categories").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/produto/cadastro/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/produto/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_EDITOR")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:9000",
                "http://localhost:5173",
                "https://artesanaldoceria.roboticsmind.com.br"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            Stream<String> realmRoles = Stream.empty();
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                final Collection<String> roles = (Collection<String>) realmAccess.get("roles");
                realmRoles = roles.stream();
            }

            final Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            Stream<String> clientRoles = Stream.empty();
            if (resourceAccess != null && resourceAccess.containsKey("quasar-app")) {
                final Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("quasar-app");
                if (clientAccess.containsKey("roles")) {
                    final Collection<String> roles = (Collection<String>) clientAccess.get("roles");
                    clientRoles = roles.stream();
                }
            }

            return Stream.concat(realmRoles, clientRoles)
                    .map(roleName -> "ROLE_" + roleName.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return converter;
    }
}