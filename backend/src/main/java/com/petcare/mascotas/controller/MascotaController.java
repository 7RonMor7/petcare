package com.petcare.mascotas.controller;

import com.petcare.mascotas.dto.MascotaRequest;
import com.petcare.mascotas.dto.MascotaResponse;
import com.petcare.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MASCOTA_CREAR')")
    public MascotaResponse registrar(@Valid @RequestBody MascotaRequest datos,
                                     Authentication autenticacion) {
        Long clienteId = (Long) autenticacion.getPrincipal();
        return mascotaService.registrar(datos, clienteId);
    }
}
