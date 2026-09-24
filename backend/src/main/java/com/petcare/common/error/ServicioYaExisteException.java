package com.petcare.common.error;

public class ServicioYaExisteException extends RuntimeException {
    public ServicioYaExisteException(String mensaje) {
        super(mensaje);
    }
}
