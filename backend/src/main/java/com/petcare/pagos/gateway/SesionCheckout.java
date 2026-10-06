package com.petcare.pagos.gateway;

/** Lo que devuelve la pasarela al abrir un cobro. */
public record SesionCheckout(String referenciaExterna, String urlCheckout) {}
