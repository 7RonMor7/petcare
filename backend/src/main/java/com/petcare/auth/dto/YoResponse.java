package com.petcare.auth.dto;

import java.util.List;

public record YoResponse(
        Long id,
        String correo,
        String nombre,
        String apellido,
        List<String> roles,
        List<String> permisos
) {
}
