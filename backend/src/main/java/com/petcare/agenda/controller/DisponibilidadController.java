package com.petcare.agenda.controller;

import com.petcare.agenda.dto.AsignacionResponse;
import com.petcare.agenda.dto.FranjaDisponibleResponse;
import com.petcare.agenda.service.DisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    @GetMapping
    @PreAuthorize("hasAuthority('DISPONIBILIDAD_CONSULTAR')")
    public List<FranjaDisponibleResponse> consultar(
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long empleadoId) {
        return disponibilidadService.consultar(servicioId, fecha, empleadoId);
    }

    @GetMapping("/asignacion")
    @PreAuthorize("hasAuthority('DISPONIBILIDAD_CONSULTAR')")
    public AsignacionResponse asignar(
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio) {
        return disponibilidadService.asignar(servicioId, fecha, horaInicio);
    }
}
