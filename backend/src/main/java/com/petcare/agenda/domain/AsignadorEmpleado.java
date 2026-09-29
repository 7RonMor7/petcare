package com.petcare.agenda.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Política de asignación automática. Pura: entra información, sale una decisión. */
public final class AsignadorEmpleado {

    private AsignadorEmpleado() {}

    /** El menos ocupado del día; a igual carga, el de id menor (estable y predecible). */
    public static Optional<Long> elegir(List<Long> candidatos, Map<Long, Long> cargarDelDia) {
        return candidatos.stream()
                .min(Comparator.<Long>comparingLong(id -> cargarDelDia.getOrDefault(id, 0L))
                        .thenComparing(Comparator.naturalOrder()));
    }
}
