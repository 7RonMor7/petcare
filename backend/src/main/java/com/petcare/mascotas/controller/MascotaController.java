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

import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasAuthority('MASCOTA_LEER_PROPIA')")
    public List<MascotaResponse> listar(Authentication autenticacion) {
        return mascotaService.listar((Long) autenticacion.getPrincipal());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MASCOTA_LEER_PROPIA')")
    public MascotaResponse obtener(@PathVariable Long id, Authentication autenticacion) {
        return mascotaService.obtener(id, (Long) autenticacion.getPrincipal());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MASCOTA_EDITAR_PROPIA')")
    public MascotaResponse actualizar(@PathVariable Long id,
                                      @Valid @RequestBody MascotaRequest datos,
                                      Authentication autenticacion) {
        return mascotaService.actualizar(id, datos, (Long) autenticacion.getPrincipal());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('MASCOTA_ELIMINAR_PROPIA')")
    public void eliminar(@PathVariable Long id, Authentication autenticacion) {
        mascotaService.eliminar(id, (Long) autenticacion.getPrincipal());
    }
}
