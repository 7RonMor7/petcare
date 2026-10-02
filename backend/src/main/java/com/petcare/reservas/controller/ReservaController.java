package com.petcare.reservas.controller;

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
}