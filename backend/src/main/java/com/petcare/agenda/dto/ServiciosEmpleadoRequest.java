package com.petcare.agenda.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ServiciosEmpleadoRequest(@NotNull List<Long> servicioIds) {
}
