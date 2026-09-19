package com.petcare.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiraEn,
        UsuarioResponse usuario
) {
}
