package com.petcare.pagos.gateway;

import com.petcare.pagos.domain.OrdenPago;

public interface PaymentGateway {

    /** Nombre del proveedor, que se guarda en la orden. */
    String nombre();

    /** Abre el cobro y devuelve a dónde mandar al cliente. */
    SesionCheckout crearSesion(OrdenPago orden, String urlRetorno);

    /**
     * Valida la firma del webhook y traduce su cuerpo.
     * @throws FirmaInvalidaException si la firma no corresponde al cuerpo recibido
     */
    EventoPago interpretar(String cuerpo, String firmaRecibida);
}
