package com.petcare.agenda.dto;

import com.petcare.agenda.domain.JornadaLaboral;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TramoResponse(Long id, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
    public static TramoResponse desde (JornadaLaboral j) {
        return new TramoResponse(j.getId(), j.getDiaSemana(), j.getHoraInicio(), j.getHoraFin());
    }
}
