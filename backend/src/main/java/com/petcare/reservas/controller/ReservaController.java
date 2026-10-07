package com.petcare.reservas.controller;

import com.petcare.pagos.dto.CheckoutResponse;
import com.petcare.pagos.dto.OrdenPagoResponse;
import com.petcare.pagos.service.CheckoutService;
import com.petcare.pagos.service.OrdenPagoService;
import com.petcare.reservas.dto.ReservaRequest;
import com.petcare.reservas.dto.ReservaResponse;
import com.petcare.reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final OrdenPagoService ordenPagoService;
    private final CheckoutService checkoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('RESERVA_CREAR')")
    public ReservaResponse crear(@Valid @RequestBody ReservaRequest datos, Authentication auth) {
        return reservaService.crear((Long) auth.getPrincipal(), datos);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RESERVA_LEER_PROPIA')")
    public List<ReservaResponse> listar(Authentication auth) {
        return reservaService.listarPropias((Long) auth.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RESERVA_LEER_PROPIA')")
    public ReservaResponse obtener(@PathVariable Long id, Authentication auth) {
        return reservaService.obtenerPropia(id, (Long)  auth.getPrincipal());
    }

    @GetMapping("/{id}/pago")
    @PreAuthorize("hasAuthority('PAGO_LEER_PROPIO')")
    public OrdenPagoResponse pago(@PathVariable Long id, Authentication auth) {
        return ordenPagoService.ordenVigenteDe(id, (Long) auth.getPrincipal());
    }

    @PostMapping("/{id}/pago/checkout")
    @PreAuthorize("hasAuthority('RESERVA_CREAR')")
    public CheckoutResponse checkout(@PathVariable Long id, Authentication auth) {
        return checkoutService.iniciar(id, (Long) auth.getPrincipal());
    }
}