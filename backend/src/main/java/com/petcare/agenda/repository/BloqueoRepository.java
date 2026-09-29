package com.petcare.agenda.repository;

import com.petcare.agenda.domain.BloqueoAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BloqueoRepository extends JpaRepository<BloqueoAgenda, Long> {

    @Query("""
        SELECT b FROM BloqueoAgenda b
        WHERE b.empleado.id IN :empleadoIds
          AND b.fechaHoraFin > :desde
          AND b.fechaHoraInicio < :hasta
        """)
    List<BloqueoAgenda> ocupacion(@Param("empleadoIds") List<Long> empleadoIds,
                                  @Param("desde") Instant desde,
                                  @Param("hasta") Instant hasta);

    @Query("""
        SELECT b FROM BloqueoAgenda b
        WHERE b.empleado.id = empleadoId
          AND b.fechaHoraFin > :desde
          AND b.fechaHoraInicio < :hasta
        ORDER BY b.fechaHoraInicio
        """)
    List<BloqueoAgenda> deEmpleado(@Param("empleadoId") Long empleadoId,
                                   @Param("desde") Instant desde,
                                   @Param("hasta") Instant hasta);

    Optional<BloqueoAgenda> findByIdAndEmpleadoId(Long id, Long empleadoId);
}
