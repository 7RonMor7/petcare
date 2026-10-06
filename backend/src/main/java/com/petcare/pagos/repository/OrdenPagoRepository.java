package com.petcare.pagos.repository;

import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenPagoRepository extends JpaRepository<OrdenPago, Long> {

    Optional<OrdenPago> findByReservaIdAndEstado(Long reservaId, EstadoOrdenPago estado);

    List<OrdenPago> findByReservaIdOrderByCreadoEnDesc(Long reservaId);

    Optional<OrdenPago> findByReferenciaExterna(String referenciaExterna);
}
