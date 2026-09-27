package com.petcare.agenda.repository;

import com.petcare.agenda.domain.BloqueoAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

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
}
