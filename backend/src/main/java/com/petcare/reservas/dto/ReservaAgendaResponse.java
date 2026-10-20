package com.petcare.reservas.dto;

import com.petcare.mascotas.domain.Especie;
import com.petcare.mascotas.domain.Mascota;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public record ReservaAgendaResponse(
        Long id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
        EstadoReserva estado, String servicio,
        String mascota, Especie especie, String raza, String observaciones,
        String cliente
) {
    public static ReservaAgendaResponse desde(Reserva r, ZoneId zona) {
        LocalDateTime inicio = LocalDateTime.ofInstant(r.getFechaHoraInicio(), zona);
        LocalDateTime fin = LocalDateTime.ofInstant(r.getFechaHoraFin(), zona);
        Mascota m = r.getMascota();
        return new ReservaAgendaResponse(
                r.getId(), inicio.toLocalDate(), inicio.toLocalTime(), fin.toLocalTime(),
                r.getEstado(), r.getServicio().getNombre(),
                m.getNombre(), m.getEspecie(), m.getRaza(), m.getObservaciones(),
                r.getCliente().getNombre() + " " + r.getCliente().getApellido());
    }
}
