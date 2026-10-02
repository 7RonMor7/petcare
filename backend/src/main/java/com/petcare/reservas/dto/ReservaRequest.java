package com.petcare.reservas.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaRequest(
        @NotNull Long mascotaId,
        @NotNull Long servicioId,
        @NotNull LocalDate fecha,
        @NotNull LocalTime horaInicio,
        Long empleadoId                            // null = sin preferencia (HU-038)
        ) {
}
