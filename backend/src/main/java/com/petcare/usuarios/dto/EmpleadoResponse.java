package com.petcare.usuarios.dto;

import com.petcare.usuarios.domain.Usuario;

public record EmpleadoResponse(
        Long id, String nombre, String apellido, String correo, String telefono, boolean activo
) {
    public static EmpleadoResponse desde(Usuario u) {
        return new EmpleadoResponse(u.getId(), u.getNombre(), u.getApellido(),
                u.getCorreo(), u.getTelefono(), u.isActivo());
    }
}
