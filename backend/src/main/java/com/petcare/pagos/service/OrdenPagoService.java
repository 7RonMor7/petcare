package com.petcare.pagos.service;

import com.petcare.common.error.ConflictoException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.parametros.ParametroService;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import com.petcare.pagos.dto.OrdenPagoResponse;
import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.reservas.domain.Reserva;
import com.petcare.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class OrdenPagoService {

    private final OrdenPagoRepository ordenPagoRepository;
    private final ReservaRepository reservaRepository;
    private final ParametroService parametros;

    /** Se llama al crear la reserva: toda reserva nace con su orden pendiente. */
    @Transactional
    public OrdenPago emitirPara(Reserva reserva) {
        int minutos = parametros.entero(ParametroService.EXPIRACION_PAGO, 15);
        Instant limite = (reserva.getCreadoEn() != null ? reserva.getCreadoEn() : Instant.now())
                .plus(minutos, ChronoUnit.MINUTES);
        return ordenPagoRepository.save(
                new OrdenPago(reserva, limite));
    }

    @Transactional(readOnly = true)
    public OrdenPagoResponse ordenVigenteDe(Long reservaId, Long clienteId) {
        reservaRepository.findByIdAndClienteId(reservaId, clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La reserva no existe"));

        OrdenPago orden = ordenPagoRepository
                .findByReservaIdAndEstado(reservaId, EstadoOrdenPago.PENDIENTE)
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay una orden de pago pendiente"));

        if (orden.estaVencida(Instant.now())) {
            throw new ConflictoException("ORDEN_VENCIDA",
                    "La orden de pago expiró. La reserva se liberará en breve.");
        }
        return OrdenPagoResponse.desde(orden);
    }
}
