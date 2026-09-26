package com.petcare.agenda.dto;

import java.time.LocalTime;
import java.util.List;

public record FranjaDisponibleResponse(LocalTime horaInicio, LocalTime horaFin, List<Long> empleadoIds) {
}
