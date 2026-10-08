package com.petcare.reservas.repository;

import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
            SELECT r FROM Reserva r
            WHERE r.empleado.id IN :empleadoIds
              AND r.estado IN :estados
              AND r.fechaHoraFin > :desde
              AND r.fechaHoraInicio < :hasta
            """)
    List<Reserva> ocupacion(@Param("empleadoIds") List<Long> empleadoIds,
                            @Param("estados") List<EstadoReserva> estados,
                            @Param("desde") Instant desde,
                            @Param("hasta") Instant hasta);

    @Query("""
            SELECT r.empleado.id, COUNT(r) FROM Reserva r
            WHERE r.empleado.id IN :empleadoIds
              AND r.estado IN :estados
              AND r.fechaHoraFin > :desde
              AND r.fechaHoraInicio < :hasta
            GROUP BY r.empleado.id 
            """)
    List<Object[]> cargarPorEmpleado(@Param("empleadoIds") List<Long> empleadoIds,
                                     @Param("estados") List<EstadoReserva> estados,
                                     @Param("desde") Instant desde,
                                     @Param("hasta") Instant hasta);

    List<Reserva> findByClienteIdOrderByFechaHoraInicioDesc(Long clienteId);

    Optional<Reserva> findByIdAndClienteId(Long id, Long clienteId);

    List<Reserva> findByEstadoAndCreadoEnBefore(EstadoReserva estado, Instant limite);
}
