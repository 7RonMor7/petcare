package com.petcare.reservas;

import com.petcare.reservas.domain.EstadoReserva;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.petcare.reservas.domain.EstadoReserva.*;
import static org.junit.jupiter.api.Assertions.*;

class EstadoReservaTest {

    @Test
    @DisplayName("El camino feliz está permitido paso a paso")
    void caminoFeliz() {
        assertTrue(PENDIENTE_PAGO.puedePasarA(CONFIRMADA));
        assertTrue(CONFIRMADA.puedePasarA(EN_PROCESO));
        assertTrue(EN_PROCESO.puedePasarA(COMPLETADA));
    }

    @Test
    @DisplayName("No se puede completar un servicio sin pagar (RB03)")
    void sinPagoNoHayServicio() {
        assertFalse(PENDIENTE_PAGO.puedePasarA(EN_PROCESO));
        assertFalse(PENDIENTE_PAGO.puedePasarA(COMPLETADA));
    }

    @Test
    @DisplayName("Los estados finales no tienen salida")
    void estadosFinales() {
        for (EstadoReserva e : List.of(COMPLETADA, CANCELADA, EXPIRADA, NO_ASISTIO)) {
            assertTrue(e.esFinal());
            for (EstadoReserva destino : EstadoReserva.values()) {
                assertFalse(e.puedePasarA(destino), e + " no debería ir a " + destino);
            }
        }
    }

    @Test
    @DisplayName("Solo los tres estados activos ocupan agenda")
    void ocupacionDeAgenda() {
        assertTrue(CONFIRMADA.ocupaAgenda());
        assertTrue(PENDIENTE_PAGO.ocupaAgenda());
        assertFalse(CANCELADA.ocupaAgenda());
        assertFalse(COMPLETADA.ocupaAgenda());
    }

    @Test
    @DisplayName("Todos los estados están declarados en el mapa de transiciones")
    void ningunEstadoSeQuedaFuera() {
        for (EstadoReserva e : EstadoReserva.values()) {
            assertDoesNotThrow(() -> e.puedePasarA(CANCELADA));
        }
    }

}
