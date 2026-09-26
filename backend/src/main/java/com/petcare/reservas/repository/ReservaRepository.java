package com.petcare.reservas.repository;

import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservaRepository {

    @Query("""
            SELECT r FROM Reserva r
            WHERE r.empleado.id IN :empleadoIds
              AND r.estado IN :estado
              AND r.fechaHoraFin > :desde
              AND r.fechaHoraInicio < :hasta
            """)
    List<Reserva> ocupacion(@Param("empleadoIds") List<Long> empleadoIds,
                            @Param("estados") List<EstadoReserva> estados,
                            @Param("desde") Instant desde,
                            @Param("hasta") Instant hasta);
}
