package com.petcare.agenda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record JornadaRequest(
        @NotNull @Valid List<TramoRequest> tramos
) {
}
