package com.petcare.pagos.gateway;

import com.petcare.pagos.domain.EstadoOrdenPago;
import java.math.BigDecimal;

/** Lo que el webhook nos cuenta, ya traducido al lenguaje de PetCare. */
public record EventoPago(String referenciaExterna, EstadoOrdenPago estado,
                         BigDecimal monto, String moneda) {}
