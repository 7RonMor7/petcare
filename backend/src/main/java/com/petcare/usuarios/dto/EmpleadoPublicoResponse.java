package com.petcare.usuarios.dto;

import com.petcare.usuarios.domain.Usuario;

public record EmpleadoPublicoResponse(Long id, String nombre, String apellido) {
    public static EmpleadoPublicoResponse desde (Usuario u) {
        return new EmpleadoPublicoResponse(u.getId(), u.getNombre(), u.getApellido());
    }
}
