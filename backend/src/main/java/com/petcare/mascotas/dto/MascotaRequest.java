package com.petcare.mascotas.dto;

import com.petcare.mascotas.domain.Especie;
import com.petcare.mascotas.domain.Sexo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MascotaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 60, message = "Máximo 60 caracteres")
        String nombre,

        @NotNull(message = "Elige la especie")
        Especie especie,

        @Size(max = 60, message = "Máximo 60 caracteres")
        String raza,

        @NotNull(message = "Elige el sexo")
        Sexo sexo,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura")
        LocalDate fechaNacimiento,

        @NotNull(message = "El peso es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor que cero")
        @DecimalMax(value = "200.00", message = "El peso máximo es 200 kg")
        @Digits(integer = 3, fraction = 2)
        BigDecimal pesoKg,

        @Size(max = 500, message = "Máximo 500 caracteres")
        String observaciones
) {
}
