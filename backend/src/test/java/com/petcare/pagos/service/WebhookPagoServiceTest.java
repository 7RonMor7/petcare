package com.petcare.pagos.service;

import com.petcare.common.error.ConflictoException;
import com.petcare.common.error.FirmaInvalidaException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.mascotas.domain.Mascota;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import com.petcare.pagos.gateway.EventoPago;
import com.petcare.pagos.gateway.PaymentGateway;
import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import com.petcare.servicios.domain.Servicio;
import com.petcare.usuarios.domain.Usuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HU-041: firma, idempotencia y contraste de monto")
class WebhookPagoServiceTest {

    private static final String REF = "SIM-abc";
    private static final BigDecimal TOTAL = new BigDecimal("25000.00");

    @Mock private OrdenPagoRepository ordenPagoRepository;
    @Mock private PaymentGateway pasarela;

    @InjectMocks private WebhookPagoService servicio;

    private Reserva reserva;
    private OrdenPago orden;

    @BeforeEach
    void preparar() {
        reserva = new Reserva(mock(Usuario.class), mock(Mascota.class), mock(Servicio.class),
                mock(Usuario.class),
                Instant.parse("2026-10-19T14:00:00Z"), Instant.parse("2026-10-19T15:00:00Z"),
                TOTAL);
        orden = new OrdenPago(reserva, Instant.now().plus(15, ChronoUnit.MINUTES));
    }

    private void cuandoLlegue(EstadoOrdenPago estado, BigDecimal monto, String moneda) {
        when(pasarela.interpretar(any(), any()))
                .thenReturn(new EventoPago(REF, estado, monto, moneda));
        when(ordenPagoRepository.buscarParaProcesar(REF)).thenReturn(Optional.of(orden));
    }

    @Test
    @DisplayName("Pago aprobado: la orden se aprueba y la reserva se confirma (RB03)")
    void aprobadoConfirmaLaReserva() {
        cuandoLlegue(EstadoOrdenPago.APROBADO, TOTAL, "COP");

        assertEquals("APROBADO", servicio.procesar("{}", "firma-valida"));
        assertEquals(EstadoOrdenPago.APROBADO, orden.getEstado());
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    @Test
    @DisplayName("Pago rechazado: la reserva sigue pendiente para que el cliente reintente")
    void rechazadoNoCancelaLaReserva() {
        cuandoLlegue(EstadoOrdenPago.RECHAZADO, TOTAL, "COP");

        assertEquals("RECHAZADO", servicio.procesar("{}", "firma-valida"));
        assertEquals(EstadoOrdenPago.RECHAZADO, orden.getEstado());
        assertEquals(EstadoReserva.PENDIENTE_PAGO, reserva.getEstado());
    }

    @Test
    @DisplayName("Monto menor al de la orden: se rechaza y NO se confirma nada")
    void montoManipulado() {
        cuandoLlegue(EstadoOrdenPago.APROBADO, new BigDecimal("1.00"), "COP");

        ConflictoException ex = assertThrows(ConflictoException.class,
                () -> servicio.procesar("{}", "firma-valida"));

        assertEquals("MONTO_NO_COINCIDE", ex.getCodigo());
        assertEquals(EstadoOrdenPago.PENDIENTE, orden.getEstado());
        assertEquals(EstadoReserva.PENDIENTE_PAGO, reserva.getEstado());
    }

    @Test
    @DisplayName("Otra moneda por el mismo número tampoco pasa")
    void monedaDistinta() {
        cuandoLlegue(EstadoOrdenPago.APROBADO, TOTAL, "USD");

        assertThrows(ConflictoException.class, () -> servicio.procesar("{}", "firma-valida"));
        assertEquals(EstadoOrdenPago.PENDIENTE, orden.getEstado());
    }

    @Test
    @DisplayName("Reintento de la pasarela: 'YA_PROCESADO' y nada cambia")
    void idempotencia() {
        orden.cambiarEstado(EstadoOrdenPago.APROBADO);
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
        cuandoLlegue(EstadoOrdenPago.APROBADO, TOTAL, "COP");

        assertEquals("YA_PROCESADO", servicio.procesar("{}", "firma-valida"));
        assertEquals(EstadoOrdenPago.APROBADO, orden.getEstado());
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    @Test
    @DisplayName("Referencia desconocida: 404")
    void referenciaDesconocida() {
        when(pasarela.interpretar(any(), any()))
                .thenReturn(new EventoPago(REF, EstadoOrdenPago.APROBADO, TOTAL, "COP"));
        when(ordenPagoRepository.buscarParaProcesar(REF)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.procesar("{}", "firma-valida"));
    }

    @Test
    @DisplayName("Firma inválida: ni siquiera se consulta la base")
    void firmaInvalida() {
        when(pasarela.interpretar(any(), any()))
                .thenThrow(new FirmaInvalidaException("Firma no válida"));

        assertThrows(FirmaInvalidaException.class, () -> servicio.procesar("{}", "firma-falsa"));
        verify(ordenPagoRepository, never()).buscarParaProcesar(any());
    }
}
