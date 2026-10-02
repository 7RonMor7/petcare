package com.petcare.reservas.dto;

import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public record ReservaResponse(
        Long id, String servicio, String mascota, String empleado,
        LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
        EstadoReserva estado, BigDecimal total
) {
    public static ReservaResponse desde (Reserva r, ZoneId zona) {
        LocalDateTime inicio = LocalDateTime.ofInstant(r.getFechaHoraFin(), zona);
        LocalDateTime fin = LocalDateTime.ofInstant(r.getFechaHoraFin(), zona);
        return new ReservaResponse(
                r.getId(), r.getServicio().getNombre(), r.getMascota().getNombre(),
                r.getEmpleado().getNombre() + " " + r.getEmpleado().getApellido(),
                inicio.toLocalDate(), inicio.toLocalTime(), fin.toLocalTime(),
                r.getEstado(), r.getTotal());
    }
}
