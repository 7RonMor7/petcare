package com.petcare.pagos.controller;

import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.pagos.domain.OrdenPago;
import com.petcare.pagos.dto.SimularPagoRequest;
import com.petcare.pagos.dto.WebhookResponse;
import com.petcare.pagos.gateway.PasarelaSimulada;
import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.pagos.service.WebhookPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhooks/pagos/simular")
@ConditionalOnProperty(name = "petcare.pagos.proveedor", havingValue = "simulado", matchIfMissing = true)
@RequiredArgsConstructor
public class SimuladorPagoController {

    private final WebhookPagoService webhookPagoService;
    private final PasarelaSimulada pasarela;
    private final OrdenPagoRepository ordenPagoRepository;

    @PostMapping
    public WebhookResponse simular(@Valid @RequestBody SimularPagoRequest datos) {
        OrdenPago orden = ordenPagoRepository.findByReferenciaExterna(datos.referencia())
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de pago no encontrada"));

        String cuerpo = """
                {"referencia":"%s","estado":"%s","monto":%s,"moneda":"%s"}"""
                .formatted(datos.referencia(), datos.estado(), orden.getMonto(), orden.getMoneda());

        return new WebhookResponse(webhookPagoService.procesar(cuerpo, pasarela.firmar(cuerpo)));
    }
}
