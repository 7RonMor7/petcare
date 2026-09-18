package com.petcare.auth.dto;

import java.util.Set;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String correo,
        Set<String> roles
) {
}
