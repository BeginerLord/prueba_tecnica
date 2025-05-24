package com.broers.prueba_tecnica.security.config;

import com.broers.prueba_tecnica.utils.constants.EndpointsConstants;
import com.broers.prueba_tecnica.security.utils.jwt.JwtTokenProvider;
import com.broers.prueba_tecnica.security.utils.jwt.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(http -> {

                    // Endpoints públicos
                    http.requestMatchers(HttpMethod.POST, EndpointsConstants.ENDPOINT_SIGNUP).permitAll();
                    http.requestMatchers(HttpMethod.POST, EndpointsConstants.ENDPOINT_LOGIN).permitAll();
                    http.requestMatchers(EndpointsConstants.ENDPOINT_RESET_FORM).permitAll();

                    // Endpoints para verificación de email y gestión de contraseña
                    http.requestMatchers(HttpMethod.GET, EndpointsConstants.ENDPOINT_VERIFY_EMAIL).permitAll();
                    http.requestMatchers(HttpMethod.POST, EndpointsConstants.ENDPOINT_SET_PASSWORD).permitAll();
                    http.requestMatchers(HttpMethod.POST, EndpointsConstants.ENDPOINT_REQUEST_PASSWORD_RECOVERY).permitAll();
                    http.requestMatchers(HttpMethod.POST, EndpointsConstants.ENDPOINT_RESET_PASSWORD).permitAll();

                    // Endpoints Swagger
                    http.requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/v3/api-docs.json"
                    ).permitAll();

                    // Endpoints solo para usuarios autenticados con rol USER
                    http.requestMatchers(EndpointsConstants.ENDPOINT_USERS + "/**").hasAuthority("ROLE_USER");

                    // Cualquier otra petición requiere autenticación
                    http.anyRequest().authenticated();
                })

                .addFilterBefore(new JwtTokenValidator(jwtTokenProvider), BasicAuthenticationFilter.class)
                .build();
    }
}
