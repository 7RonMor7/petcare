package com.petcare.pagos.dto;

import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

public record OrdenPagoResponse(
        Long id, Long reservaId, BigDecimal monto, String moneda,
        EstadoOrdenPago estado, Instant expiraEn, long segundosRestantes
) {
    public static OrdenPagoResponse desde (OrdenPago o) {
        long restantes = Math.max(0, Duration.between(Instant.now(), o.getExpiraEn()).getSeconds());
        return new OrdenPagoResponse(o.getId(), o.getReserva().getId(), o.getMonto(),
                o.getMoneda(), o.getEstado(), o.getExpiraEn(), restantes);
    }
}
