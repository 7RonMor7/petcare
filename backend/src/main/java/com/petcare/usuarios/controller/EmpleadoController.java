package com.petcare.usuarios.controller;

import com.petcare.agenda.dto.JornadaRequest;
import com.petcare.agenda.dto.ServiciosEmpleadoRequest;
import com.petcare.agenda.dto.TramoResponse;
import com.petcare.agenda.service.EmpleadoServicioService;
import com.petcare.agenda.service.JornadaService;
import com.petcare.common.dto.EstadoRequest;
import com.petcare.servicios.dto.ServicioResponse;
import com.petcare.usuarios.dto.EmpleadoActualizarRequest;
import com.petcare.usuarios.dto.EmpleadoRequest;
import com.petcare.usuarios.dto.EmpleadoResponse;
import com.petcare.usuarios.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final JornadaService jornadaService;
    private final EmpleadoServicioService empleadoServicioService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public List<EmpleadoResponse> listar() { return empleadoService.listar(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public EmpleadoResponse crear(@Valid @RequestBody EmpleadoRequest datos) {
        return empleadoService.crear(datos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public EmpleadoResponse actualizar(@PathVariable Long id,
                                       @Valid @RequestBody EmpleadoActualizarRequest datos) {
        return empleadoService.actualizar(id, datos);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public EmpleadoResponse cambiarEsatdo(@PathVariable Long id,
                                          @Valid @RequestBody EstadoRequest datos) {
        return empleadoService.cambiarEstado(id, datos.activo());
    }

    @GetMapping("/{id}/jornada")
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public List<TramoResponse> consultarJornada(@PathVariable Long id) {
        return jornadaService.consultar(id);
    }

    @PutMapping("/{id}/jornada")
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public List<TramoResponse> guardarJornada(@PathVariable Long id,
                                              @Valid @RequestBody JornadaRequest datos) {
        return jornadaService.reemplazar(id, datos.tramos());
    }

    @GetMapping("/{id}/servicios")
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public List<ServicioResponse> consultarServicios(@PathVariable Long id) {
        return empleadoServicioService.consultar(id);
    }

    @PutMapping("/{id}/servicios")
    @PreAuthorize("hasAuthority('EMPLEADO_GESTIONAR')")
    public List<ServicioResponse> guardarServicios(@PathVariable Long id,
                                                   @Valid @RequestBody ServiciosEmpleadoRequest datos) {
        return empleadoServicioService.reemplazar(id, datos.servicioIds());
    }
}
