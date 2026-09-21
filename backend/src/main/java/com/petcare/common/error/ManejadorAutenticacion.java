package com.petcare.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class ManejadorAutenticacion implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper mapper;

    public ManejadorAutenticacion(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /** Se invoca cuando la peticion no esta autenticada y la ruta lo exige. */
    @Override
    public void commence(HttpServletRequest peticion, HttpServletResponse respuesta,
                         AuthenticationException ex) throws IOException {

        String motivo = (String) peticion.getAttribute("errorJwt");

        String codigo = "ExpiredJwtException".equals(motivo)
                ? "TOKEN_EXPIRADO"
                : (motivo != null ? "TOKEN_INVALIDO" : "NO_AUTENTICADO");

        escribir(peticion, respuesta, HttpStatus.UNAUTHORIZED, codigo,
                "Se requiere autenticación para acceder a este recurso");
    }

    /** Se invoca cuando la peticion esta autenticada pero le falta el permiso. */
    @Override
    public void handle(HttpServletRequest peticion, HttpServletResponse respuesta,
                       AccessDeniedException ex) throws IOException {

        escribir(peticion, respuesta, HttpStatus.FORBIDDEN, "ACCESO_DENEGADO",
                "No tiene permisos para realizar esta operación");
    }

    private void escribir(HttpServletRequest peticion, HttpServletResponse respuesta,
                          HttpStatus estado, String codigo, String mensaje) throws IOException {

        RespuestaError cuerpo = new RespuestaError(
                Instant.now(), estado.value(), codigo, mensaje,
                peticion.getRequestURI(), null);

        respuesta.setStatus(estado.value());
        respuesta.setContentType(MediaType.APPLICATION_JSON_VALUE);
        respuesta.setCharacterEncoding("UTF-8");
        mapper.writeValue(respuesta.getOutputStream(), cuerpo);
    }
}
