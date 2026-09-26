package com.petcare.reservas.domain;

import java.util.List;

public enum EstadoReserva {
    PENDIENTE_PAGO, CONFIRMADA, EN_PROCESO, COMPLETADA, CANCELADA, EXPIRADA, NO_ASISTIO;

    // Estados que ocupan la agenda (RNF07-R del análisis).
    public static final List<EstadoReserva> ACTIVOS =
            List.of(PENDIENTE_PAGO, CONFIRMADA, EN_PROCESO);
}
