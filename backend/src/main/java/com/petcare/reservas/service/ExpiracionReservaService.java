package com.petcare.reservas.service;

import com.petcare.common.parametros.ParametroService;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import com.petcare.reservas.repository.OcupacionFranjaRepository;
import com.petcare.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpiracionReservaService {

    private final ReservaRepository reservaRepository;
    private final OrdenPagoRepository ordenPagoRepository;
    private final OcupacionFranjaRepository  ocupacionRepository;
    private final ParametroService parametros;

    @Scheduled(fixedDelayString = "${petcare.expiracion.cada-ms:60000}")
    @Transactional
    public void expirarVencidas() {
        int minutos = parametros.entero(ParametroService.EXPIRACION_PAGO, 15);
        Instant limite = Instant.now().minus(minutos, ChronoUnit.MINUTES);

        List<Reserva> vencidas = reservaRepository
                .findByEstadoAndCreadoEnBefore(EstadoReserva.PENDIENTE_PAGO, limite);

        for (Reserva reserva : vencidas) {
            reserva.cambiarEstado(EstadoReserva.EXPIRADA);

            ordenPagoRepository.findByReservaIdOrderByCreadoEnDesc(reserva.getId()).stream()
                    .filter(o -> o.getEstado() == EstadoOrdenPago.PENDIENTE)
                    .forEach(o -> o.cambiarEstado(EstadoOrdenPago.EXPIRADO));

            ocupacionRepository.deleteByReservaId(reserva.getId());   // <- se libera la franja

            log.info("Reserva {} expirada por falta de ´pago: franja liberada", reserva.getId());
        }

        if (!vencidas.isEmpty()) {
            log.info("Expiradas {} reservas sin pagar", vencidas.size());
        }
    }
}
