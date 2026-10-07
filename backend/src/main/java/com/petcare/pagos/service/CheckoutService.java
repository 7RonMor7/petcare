package com.petcare.pagos.service;

import com.petcare.common.error.ConflictoException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import com.petcare.pagos.dto.CheckoutResponse;
import com.petcare.pagos.gateway.PaymentGateway;
import com.petcare.pagos.gateway.SesionCheckout;
import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import com.petcare.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ReservaRepository reservaRepository;
    private final OrdenPagoRepository ordenPagoRepository;
    private final PaymentGateway pasarela;             // <- el puerto, no una implementación

    @Value("${petcare.pagos.url-retorno}")
    private String urlRetorno;

    @Transactional
    public CheckoutResponse iniciar(Long reservaId, Long clienteId) {
        Reserva reserva = reservaRepository.findByIdAndClienteId(reservaId, clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La reserva no existe"));

        if (reserva.getEstado() != EstadoReserva.PENDIENTE_PAGO) {
            throw new ConflictoException("RESERVA_NO_PAGABLE",
                    "Esta reserva está en estado " + reserva.getEstado());
        }

        OrdenPago orden = ordenPagoRepository
                .findByReservaIdAndEstado(reservaId, EstadoOrdenPago.PENDIENTE)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay orden de pago pendiente"));

        if (orden.estaVencida(Instant.now())) {
            throw new ConflictoException("ORDEN_VENCIDA",
                    "La orden de pago expiró. La reserva se libererá en breve.");
        }

        if (!orden.tieneCheckoutAbierto()) {
            SesionCheckout sesion = pasarela.crearSesion(orden, urlRetorno);
            orden.registrarEnPasarela(pasarela.nombre(), sesion.referenciaExterna(), sesion.urlCheckout());
        }

        long restantes = Math.max(0, Duration.between(Instant.now(), orden.getExpiraEn()).getSeconds());
        return new CheckoutResponse(orden.getUrlCheckout(), orden.getReferenciaExterna(),
                orden.getProveedor(), restantes);
    }
}
