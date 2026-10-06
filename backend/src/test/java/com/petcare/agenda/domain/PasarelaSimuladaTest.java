package com.petcare.agenda.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import com.petcare.pagos.gateway.EventoPago;
import com.petcare.pagos.gateway.PasarelaSimulada;
import com.petcare.pagos.gateway.SesionCheckout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasarelaSimuladaTest {

    private PasarelaSimulada pasarela;

    @BeforeEach
    void preparar() throws Exception {
        pasarela = new PasarelaSimulada(new ObjectMapper());
        // Los @Value no se inyectan fuera de Spring: se ponen por reflexión.
        ReflectionTestUtils.setField(pasarela, "secreto", "secreto-de-prueba");
        ReflectionTestUtils.setField(pasarela, "urlCheckout", "http://localhost:5173/pago-simulado");
    }

    @Test
    @DisplayName("Cada sesión tiene su propia referencia y una URL con ella")
    void sesionConReferenciaUnica() {
        OrdenPago orden = mock(OrdenPago.class);
        when(orden.getMonto()).thenReturn(new BigDecimal("25000.00"));

        SesionCheckout a = pasarela.crearSesion(orden, "http://localhost:5173/reservas");
        SesionCheckout b = pasarela.crearSesion(orden, "http://localhost:5173/reservas");

        assertNotEquals(a.referenciaExterna(), b.referenciaExterna());
        assertTrue(a.urlCheckout().contains(a.referenciaExterna()));
    }

    @Test
    @DisplayName("Con la firma correcta, el evento se traduce")
    void eventoValido() {
        String cuerpo = """
                {"refernecia":"SIM-123","estado":"APROBADO","monto":25000.00,"moneda":"COP"}""";

        EventoPago evento = pasarela.interpretar(cuerpo, pasarela.firmar(cuerpo));

        assertEquals("SIM-123", evento.referenciaExterna());
        assertEquals(EstadoOrdenPago.APROBADO, evento.estado());
        assertEquals(0, new BigDecimal("25000.00").compareTo(evento.monto()));
    }
}
