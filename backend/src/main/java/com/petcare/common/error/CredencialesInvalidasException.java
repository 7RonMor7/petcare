package com.petcare.common.error;

public class CredencialesInvalidasException extends RuntimeException{

    public CredencialesInvalidasException() {
        super("Correo o contraseña incorrectos");
    }
}
