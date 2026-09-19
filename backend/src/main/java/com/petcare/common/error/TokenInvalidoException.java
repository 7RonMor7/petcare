package com.petcare.common.error;

public class TokenInvalidoException extends RuntimeException {

    public TokenInvalidoException() {
        super("El token de refresco no es válido o ha expirado");
    }
}
