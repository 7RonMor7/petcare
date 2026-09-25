package com.petcare.servicios.controller;

import com.petcare.common.dto.EstadoRequest;
import com.petcare.servicios.dto.ServicioAdminResponse;
import com.petcare.servicios.dto.ServicioRequest;
import com.petcare.servicios.dto.ServicioResponse;
import com.petcare.servicios.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    public List<ServicioResponse> listar() {
        return servicioService.listarCatalogo();
    }

    @GetMapping("/{id}")
    public ServicioResponse obtener(@PathVariable Long id) {
        return servicioService.obtenerDelCatalogo(id);
    }

    @GetMapping("/gestion")
    @PreAuthorize("hasAuthority('SERVICIO_GESTIONAR')")
    public List<ServicioAdminResponse> listarTodos() {
        return servicioService.listarTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SERVICIO_GESTIONAR')")
    public ServicioAdminResponse crear(@Valid @RequestBody ServicioRequest datos) {
        return servicioService.crear(datos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_GESTIONAR')")
    public ServicioAdminResponse actualizar(@PathVariable Long id,
                                            @Valid @RequestBody ServicioRequest datos) {
        return servicioService.actualizar(id, datos);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('SERVICIO_GESTIONAR')")
    public ServicioAdminResponse cambiarEstado(@PathVariable Long id,
                                               @Valid @RequestBody EstadoRequest datos) {
        return servicioService.cambiarEstado(id, datos.activo());
    }
}
