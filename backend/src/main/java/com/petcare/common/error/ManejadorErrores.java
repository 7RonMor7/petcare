package com.petcare.common.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> validacionFallida(
            MethodArgumentNotValidException ex, HttpServletRequest peticion){

        List<RespuestaError.DetalleError> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new RespuestaError.DetalleError(e.getField(), e.getDefaultMessage()))
                .toList();

        return construir(HttpStatus.BAD_REQUEST, "VALIDACION_FALLIDA",
                "La solicitud contiene campos inválidos", peticion, detalles);
    }

    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<RespuestaError> correoDuplicado(
            CorreoYaRegistradoException ex, HttpServletRequest peticion){

        return construir(HttpStatus.CONFLICT, "CORREO_YA_REGISTRADO",
                ex.getMessage(), peticion, null);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<RespuestaError> credencialesInvalidas(
            CredencialesInvalidasException ex, HttpServletRequest peticion){

        return construir(HttpStatus.UNAUTHORIZED, "CREDENCIALES_INVALIDAS",
                ex.getMessage(), peticion, null);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<RespuestaError> tokenInvalido(
            TokenInvalidoException ex, HttpServletRequest peticion){

        return construir(HttpStatus.UNAUTHORIZED, "TOKEN_INVALIDO",
                ex.getMessage(), peticion, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError> cuerpoInvalido(
            HttpMessageNotReadableException ex, HttpServletRequest peticion){

        return construir(HttpStatus.BAD_REQUEST, "CUERPO_INVALIDO",
                "El cuerpo de la petición no es válido o tiene valores no permitidos", peticion, null);
    }

    private ResponseEntity<RespuestaError> construir(
            HttpStatus estado, String codigo, String mensaje,
            HttpServletRequest peticion, List<RespuestaError.DetalleError> detalles){

        RespuestaError cuerpo = new RespuestaError(
                Instant.now(), estado.value(), codigo, mensaje,
                peticion.getRequestURI(), detalles);

        return ResponseEntity.status(estado).body(cuerpo);
    }
}
