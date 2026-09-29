package com.petcare.agenda.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueoRequest(
        @NotNull @FutureOrPresent LocalDate fecha,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        @Size(max = 200) String motivo
        ) {
}
