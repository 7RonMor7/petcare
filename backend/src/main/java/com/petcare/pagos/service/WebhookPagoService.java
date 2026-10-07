package com.petcare.pagos.service;

import com.petcare.common.error.ConflictoException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import com.petcare.pagos.gateway.EventoPago;
import com.petcare.pagos.gateway.PaymentGateway;
import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookPagoService {

    private final OrdenPagoRepository ordenPagoRepository;
    private final PaymentGateway pasarela;

    @Transactional
    public String procesar(String cuerpo, String firma) {

        EventoPago evento = pasarela.interpretar(cuerpo, firma);    // 1. firma -> 401 si falla

        OrdenPago orden = ordenPagoRepository.buscarParaProcesar(evento.referenciaExterna())
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de pago no encontrada"));

        // 2. Idempotencia: un reintento de la pasarela NO es un error
        if (orden.getEstado() != EstadoOrdenPago.PENDIENTE) {
            log.info("Webhook repetido para {} (ua estaba {})",
                    evento.referenciaExterna(), orden.getEstado());
            return "YA_PROCESADO";
        }

        // 3. Contraste de monto y moneda
        if (orden.getMonto().compareTo(evento.monto()) != 0
                || !orden.getMoneda().equals(evento.moneda())) {
            log.warn("Monto no coincide para {}: esperado {} {}, recibido {} {}",
                    evento.referenciaExterna(), orden.getMonto(), orden.getMoneda(),
                    evento.monto(), evento.moneda());
            throw new ConflictoException("MONTO_NO_COINCIDE",
                    "El monto del evento no corresponde con la orden");
        }

        // 4. Aplicar
        Reserva reserva = orden.getReserva();

        if (evento.estado() == EstadoOrdenPago.APROBADO) {
            orden.cambiarEstado(EstadoOrdenPago.APROBADO);
            reserva.cambiarEstado(EstadoReserva.CONFIRMADA);     // HU-042, RB03
            log.info("Pago aprobado {} -> reserva {} CONFIRMADA",
                    evento.referenciaExterna(), reserva.getId());
            return "APROBADO";
        }

        orden.cambiarEstado(EstadoOrdenPago.RECHAZADO);
        // La reserva sigue PENDIENTE_PAGO: el cliente puede reintentar (IRR-12)
        log.info("Pago rechazado {} -> reserva {} sigue pendiente",
                evento.referenciaExterna(), reserva.getId());
        return "RECHAZADO";
    }
}
