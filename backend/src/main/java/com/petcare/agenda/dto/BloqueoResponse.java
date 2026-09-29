package com.petcare.agenda.dto;

import com.petcare.agenda.domain.BloqueoAgenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public record BloqueoResponse(Long id, LocalDate fecha, LocalTime horaInicio,
                              LocalTime horaFin, String motivo) {

    public static BloqueoResponse desde(BloqueoAgenda b, ZoneId zona) {
        LocalDateTime inicio = LocalDateTime.ofInstant(b.getFechaHoraInicio(), zona);
        LocalDateTime fin = LocalDateTime.ofInstant(b.getFechaHoraFin(), zona);
        return new BloqueoResponse(b.getId(), inicio.toLocalDate(),
                inicio.toLocalTime(), fin.toLocalTime(), b.getMotivo());
    }
}
