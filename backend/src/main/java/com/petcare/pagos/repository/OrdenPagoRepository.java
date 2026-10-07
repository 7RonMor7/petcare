package com.petcare.pagos.repository;

import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdenPagoRepository extends JpaRepository<OrdenPago, Long> {

    Optional<OrdenPago> findByReservaIdAndEstado(Long reservaId, EstadoOrdenPago estado);

    List<OrdenPago> findByReservaIdOrderByCreadoEnDesc(Long reservaId);

    Optional<OrdenPago> findByReferenciaExterna(String referenciaExterna);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrdenPago o WHERE o.referenciaExterna = :referencia")
    Optional<OrdenPago> buscarParaProcesar(@Param("referencia") String referencia);
}
