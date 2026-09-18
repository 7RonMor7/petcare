package com.petcare.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroClienteRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80)
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 80)
        String apellido,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo no es válido")
        @Size(max = 160)
        String correo,

        @Size(max = 20)
        String telefono,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72, message = "Debe tener entre 8 y 72 caracteres")
        String contrasena,

        @AssertTrue(message = "Debe aceptar la política de tratamiento de datos")
        boolean aceptaPoliticaDatos
) {
}
