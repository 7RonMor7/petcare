package com.petcare.agenda.controller;

import com.petcare.agenda.dto.BloqueoRequest;
import com.petcare.agenda.dto.BloqueoResponse;
import com.petcare.agenda.dto.TramoResponse;
import com.petcare.agenda.service.BloqueoService;
import com.petcare.agenda.service.JornadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mi-agenda")
@RequiredArgsConstructor
public class MiAgendaController {

    private final BloqueoService bloqueoService;
    private final JornadaService jornadaService;

    @GetMapping("/jornada")
    @PreAuthorize("hasAuthority('AGENDA_LEER_PROPIA')")
    public List<TramoResponse> miJornada(Authentication auth) {
        return jornadaService.consultar((Long) auth.getPrincipal());
    }

    @GetMapping("/bloqueos")
    @PreAuthorize("hasAuthority('AGENDA_LEER_PROPIA')")
    public List<BloqueoResponse> misBloqueos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Authentication auth) {
        return bloqueoService.listar((Long)  auth.getPrincipal(), desde, hasta);
    }

    @PostMapping("/bloqueos")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('AGENDA_BLOQUEAR_PROPIA')")
    public BloqueoResponse bloquear(@Valid @RequestBody BloqueoRequest datos, Authentication auth) {
        return bloqueoService.crear((Long) auth.getPrincipal(), datos);
    }

    @DeleteMapping("/bloqueos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('AGENDA_BLOQUEAR_PROPIA')")
    public void eliminar(@PathVariable Long id, Authentication auth) {
        bloqueoService.eliminar(id, (Long) auth.getPrincipal());
    }
}
