package com.petcare.pagos.dto;

import jakarta.validation.constraints.NotBlank;

// Solo para el simulador: el frontend no puede firmar, porque no tiene el secreto.
public record SimularPagoRequest(@NotBlank String referencia, @NotBlank String estado) {}
