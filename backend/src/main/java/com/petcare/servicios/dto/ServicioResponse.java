package com.petcare.servicios.dto;

import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.domain.UnidadCobro;

import java.math.BigDecimal;

public record ServicioResponse(
        Long id, String nombre, String descripcion,
        BigDecimal precio, UnidadCobro unidadCobro, Integer duracionMinutos
) {
    public static ServicioResponse desde(Servicio s) {
        return new ServicioResponse(s.getId(), s.getNombre(), s.getDescripcion(),
                s.getPrecio(), s.getUnidadCobro(), s.getDuracionMinutos());
    }
}
