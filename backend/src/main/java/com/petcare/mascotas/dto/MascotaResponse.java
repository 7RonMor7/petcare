package com.petcare.mascotas.dto;

import com.petcare.mascotas.domain.Especie;
import com.petcare.mascotas.domain.Sexo;
import com.petcare.mascotas.domain.Mascota;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MascotaResponse(
        Long id, Long clienteId, String nombre, Especie especie, String raza,
        Sexo sexo, LocalDate fechaNacimiento, BigDecimal pesoKg,
        String observaciones, boolean activo
) {

    public static MascotaResponse desde(Mascota m) {
        return new MascotaResponse(
                m.getId(), m.getCliente().getId(), m.getNombre(), m.getEspecie(),
                m.getRaza(), m.getSexo(), m.getFechaNacimiento(), m.getPesoKg(),
                m.getObservaciones(), m.isActivo());
    }
}
