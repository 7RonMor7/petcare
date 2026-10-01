package com.petcare.reservas.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum EstadoReserva {
    PENDIENTE_PAGO, CONFIRMADA, EN_PROCESO, COMPLETADA, CANCELADA, EXPIRADA, NO_ASISTIO;

    // Estados que ocupan la agenda (RNF07-R).
    public static final List<EstadoReserva> ACTIVOS =
            List.of(PENDIENTE_PAGO, CONFIRMADA, EN_PROCESO);

    private static final Map<EstadoReserva , Set<EstadoReserva>> TRANSICIONES = Map.of(
            PENDIENTE_PAGO, Set.of(CONFIRMADA, CANCELADA, EXPIRADA),
            CONFIRMADA, Set.of(EN_PROCESO, CANCELADA, NO_ASISTIO),
            EN_PROCESO, Set.of(COMPLETADA),
            COMPLETADA, Set.of(),
            CANCELADA, Set.of(),
            EXPIRADA, Set.of(),
            NO_ASISTIO, Set.of()
    );

    public boolean puedePasarA(EstadoReserva destino) {
        return TRANSICIONES.get(this).contains(destino);
    }

    public boolean esFinal() {
        return TRANSICIONES.get(this).isEmpty();
    }

    public boolean ocupaAgenda() {
        return ACTIVOS.contains(this);
    }
}
