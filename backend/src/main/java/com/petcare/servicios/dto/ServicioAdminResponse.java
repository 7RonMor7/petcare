package com.petcare.servicios.dto;

import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.domain.UnidadCobro;

import java.math.BigDecimal;

public record ServicioAdminResponse(
        Long id, String nombre, String descripcion, BigDecimal precio,
        UnidadCobro unidadCobro, Integer duracionMinutos, boolean activo
) {
    public static ServicioAdminResponse desde (Servicio s) {
        return new ServicioAdminResponse(s.getId(), s.getNombre(), s.getDescripcion(),
                s.getPrecio(), s.getUnidadCobro(), s.getDuracionMinutos(), s.isActivo());
    }
}
