package com.petcare.pagos.controller;

import com.petcare.pagos.dto.WebhookResponse;
import com.petcare.pagos.service.WebhookPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/webhooks/pagos")
@RequiredArgsConstructor
public class WebhookPagoController {

    private final WebhookPagoService webhookPagoService;

    @PostMapping
    public WebhookResponse recibir(@RequestBody String cuerpo,
                                   @RequestHeader(value = "X-Firma", required = false) String firma) {
        return new WebhookResponse(webhookPagoService.procesar(cuerpo, firma));
    }
}
