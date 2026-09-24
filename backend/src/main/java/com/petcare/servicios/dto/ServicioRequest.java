package com.petcare.servicios.dto;

import com.petcare.servicios.domain.UnidadCobro;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ServicioRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80)
        String nombre,

        @Size(max = 400)
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
        @Digits(integer = 8, fraction = 2)
        BigDecimal precio,

        @NotNull(message = "La unidad de cobro es obligatoria")
        UnidadCobro unidadCobro,

        @Min(value = 15, message = "La duración mínima es de 15 minutos")
        @Max(value = 480, message = "La duración máxima es de 8 horas")
        Integer duracionMinutos
) {
    @AssertTrue(message = "Los servicios por servicio exigen duración; los de por día no la llevan")
    public boolean isDuracionCoherenteConLaUnidad() {
        if (unidadCobro == null) return true;   // ya lo reporta @NotNull
        return unidadCobro == unidadCobro.POR_SERVICIO
                ? duracionMinutos != null
                : duracionMinutos == null;
    }
}
