package com.petcare.mascotas.dto;

import com.petcare.mascotas.domain.Especie;
import com.petcare.mascotas.domain.Sexo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MascotaRequest(
        @NotBlank @Size(max = 60) String nombre,
        @NotNull Especie especie,
        @Size(max = 60) String raza,
        @NotNull Sexo sexo,
        @NotNull @PastOrPresent LocalDate fechaNacimiento,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @DecimalMax("200.00")
        @Digits(integer = 3, fraction = 2)
        BigDecimal pesoKg,
        @Size(max = 500) String observaciones
) {
}
