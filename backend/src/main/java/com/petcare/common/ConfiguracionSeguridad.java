package com.petcare.common;

import com.petcare.auth.filter.JwtAutenticationFilter;
import com.petcare.common.error.ManejadorAutenticacion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class ConfiguracionSeguridad {

    private final JwtAutenticationFilter jwtFilter;
    private final ManejadorAutenticacion manejador;

    public ConfiguracionSeguridad(JwtAutenticationFilter jwtFilter,
                                  ManejadorAutenticacion manejador) {
        this.jwtFilter = jwtFilter;
        this.manejador = manejador;
    }

    @Bean
    public PasswordEncoder codificadorContrasenas() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(rutas -> rutas
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Públicas con cualquier método (el login y el registro son POST)
                        .requestMatchers(
                                "/api/v1/auth/registro",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh",
                                "/api/v1/ping",
                                "/api/v1/ping/db",
                                "/actuator/health",
                                "/error"
                        ).permitAll()

                        // Catálogo: solo lectura pública. El POST, PUT y PATCH exigen permiso.
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/servicios",
                                "api/v1/servicios/*"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(manejador)
                        .accessDeniedHandler(manejador)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}