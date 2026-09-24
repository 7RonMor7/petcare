package com.petcare.servicios.controller;

import com.petcare.servicios.dto.ServicioResponse;
import com.petcare.servicios.service.ServicioService;
import lombok.RequiredArgsConstructor;
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
}
