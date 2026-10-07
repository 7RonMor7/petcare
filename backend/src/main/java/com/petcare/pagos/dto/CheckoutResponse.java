package com.petcare.pagos.dto;

public record CheckoutResponse(String urlCheckout, String referenciaExterna,
                               String proveedor, long segundosRestantes) {
}
