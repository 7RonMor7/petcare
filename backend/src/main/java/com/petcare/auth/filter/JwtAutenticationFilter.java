package com.petcare.auth.filter;

import com.petcare.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAutenticationFilter extends OncePerRequestFilter {

    private static final String PREFIJO = "Bearer ";

    private final JwtService jwtService;

    public JwtAutenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest peticion,
                                    @NonNull HttpServletResponse respuesta,
                                    @NonNull FilterChain cadena)
            throws ServletException, IOException {

        String cabecera = peticion.getHeader("Authorization");

        if (cabecera == null || !cabecera.startsWith(PREFIJO)) {
            cadena.doFilter(peticion, respuesta);
            return;
        }

        try {
            Claims claims = jwtService.extraerClaims(cabecera.substring(PREFIJO.length()));

            @SuppressWarnings("unchecked")
            List<String> permisos = claims.get("permisos", List.class);

            List<SimpleGrantedAuthority> autoridades = permisos == null
                    ? List.of()
                    : permisos.stream().map(SimpleGrantedAuthority::new).toList();

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(
                            Long.valueOf(claims.getSubject()), null, autoridades);

            SecurityContextHolder.getContext().setAuthentication(autenticacion);

        } catch (JwtException | IllegalArgumentException e) {
            // Token invalido, expirado o manipulado: no autenticamos y seguimos.
            // Si la ruta es protegida, el EntryPoint respondera 401.
            SecurityContextHolder.clearContext();
            peticion.setAttribute("errorJwt", e.getClass().getSimpleName());
        }

        cadena.doFilter(peticion, respuesta);
    }
}
