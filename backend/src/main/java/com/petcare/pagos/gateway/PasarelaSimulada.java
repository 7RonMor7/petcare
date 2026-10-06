package com.petcare.pagos.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.common.error.FirmaInvalidaException;
import com.petcare.pagos.domain.EstadoOrdenPago;
import com.petcare.pagos.domain.OrdenPago;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "petcare.pagos.proveedor", havingValue = "simulado", matchIfMissing = true)
public class PasarelaSimulada implements PaymentGateway {

    private final ObjectMapper jackson;

    @Value("${petcare.pagos.secreto-webhook}")
    private String secreto;

    @Value("${petcare.pagos.url-checkout}")
    private String urlCheckout;

    @Override
    public String nombre() {
        return "SIMULADO";
    }

    @Override
    public SesionCheckout crearSesion(OrdenPago orden, String urlRetorno) {
        String referencia = "SIM-" + UUID.randomUUID();
        String url = urlCheckout
                + "?ref=" + referencia
                + "&monto=" + orden.getMonto()
                + "&retorno=" + urlRetorno;
        return new SesionCheckout(referencia, url);
    }

    @Override
    public EventoPago interpretar(String cuerpo, String firmaRecibida) {
        String esperada = firmar(cuerpo);

        // Comparación en tiempo constante: un equals normal termina en cuanto
        // encuentra una diferencia, y ese tiempo filtra informacion al atacante.
        if (firmaRecibida ==  null
                || !MessageDigest.isEqual(esperada.getBytes(StandardCharsets.UTF_8),
                firmaRecibida.getBytes(StandardCharsets.UTF_8))) {
            throw new FirmaInvalidaException("Firma no válida");
        }

        try {
            CuerpoSimulado datos = jackson.readValue(cuerpo, CuerpoSimulado.class);
            return new EventoPago(datos.referencia(), EstadoOrdenPago.valueOf(datos.estado()),
                    datos.monto(), datos.moneda());
        } catch (Exception e) {
            throw new FirmaInvalidaException("Cuerpo del webhook no interpretable");
        }
    }

    /** Publico para que las pruebas y el simulador del frontend puedan firmar. */
    public String firmar(String cuerpo) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] resumen = sha.digest((cuerpo + secreto).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(resumen);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo calcular la firma", e);
        }
    }

    /** Forma del JSON que envía el simulador. */
    private record CuerpoSimulado(String referencia, String estado,
                                  BigDecimal monto, String moneda) {}
}
