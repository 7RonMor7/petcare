package com.petcare.agenda.dto;

import java.time.LocalTime;

public record AsignacionResponse(
        Long empleadoId,
        LocalTime horaInicio,
        LocalTime horaFin
) {
}
