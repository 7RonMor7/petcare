package com.petcare.common.error;

import lombok.Getter;

@Getter
public class ConflictoException extends RuntimeException {

    private final String codigo;

    public ConflictoException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }
}
