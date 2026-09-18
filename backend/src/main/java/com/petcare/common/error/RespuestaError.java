package com.petcare.common.error;

import java.time.Instant;
import java.util.List;

public record RespuestaError(
        Instant marcaTiempo,
        int estado,
        String codigo,
        String mensaje,
        String ruta,
        List<DetalleError> detalles
) {
    public record DetalleError(String campo, String mensaje) {}
}
