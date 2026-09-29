package com.petcare.agenda.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AsignadorEmpleadoTest {

    @Test
    @DisplayName("Elige al menos ocupado del día")
    void elMenosOcupado() {
        var elegido = AsignadorEmpleado.elegir(List.of(4L, 9L), Map.of(4L, 3L, 9L, 1L));
        assertEquals(9L, elegido.orElseThrow());
    }

    @Test
    @DisplayName("Un empleado sin reservas cuenta como carga cero")
    void sinReservasEsCargaCero() {
        var elegido = AsignadorEmpleado.elegir(List.of(4L, 9L), Map.of(4L,2L));
        assertEquals(9L, elegido.orElseThrow());
    }

    @Test
    @DisplayName("A igual carga gana el id menor, y el resultado es estable")
    void desempateEstable() {
        var carga = Map.of(4L, 2L, 9L, 2L);
        assertEquals(4L, AsignadorEmpleado.elegir(List.of(9L, 4L), carga).orElseThrow());
        assertEquals(4L, AsignadorEmpleado.elegir(List.of(4L, 9L), carga).orElseThrow());
    }

    @Test
    @DisplayName("Sin candidatos no hay asignación")
    void sinCandidatos() {
        assertTrue(AsignadorEmpleado.elegir(List.of(), Map.of()).isEmpty());
    }
}
