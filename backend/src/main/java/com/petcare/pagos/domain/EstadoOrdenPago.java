package com.petcare.pagos.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum EstadoOrdenPago {
    PENDIENTE, APROBADO, RECHAZADO, EXPIRADO, REEMBOLSADO;

    private static final Map<EstadoOrdenPago, Set<EstadoOrdenPago>> TRANSICIONES = Map.of(
            PENDIENTE, Set.of(APROBADO, RECHAZADO, EXPIRADO),
            APROBADO, Set.of(REEMBOLSADO),
            RECHAZADO, Set.of(),
            EXPIRADO, Set.of(),
            REEMBOLSADO, Set.of()
    );

    public boolean puedePasarA(EstadoOrdenPago destino) {
        return TRANSICIONES.get(this).contains(destino);
    }
}
