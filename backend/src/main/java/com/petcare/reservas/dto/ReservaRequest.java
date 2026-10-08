package com.petcare.reservas.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaRequest(
        @NotNull(message = "Elige la mascota")
        Long mascotaId,

        @NotNull(message = "Elige el servicio")
        Long servicioId,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "Elige la hora de inicio")
        LocalTime horaInicio,

        Long empleadoId                            // null = sin preferencia (HU-038)
        ) {
}
