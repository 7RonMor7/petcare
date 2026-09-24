package com.petcare.servicios.service;

import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.error.ServicioYaExisteException;
import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.dto.ServicioAdminResponse;
import com.petcare.servicios.dto.ServicioRequest;
import com.petcare.servicios.dto.ServicioResponse;
import com.petcare.servicios.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarCatalogo() {
        return servicioRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(ServicioResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicioResponse obtenerDelCatalogo(Long id) {
        return servicioRepository.findByIdAndActivoTrue(id)
                .map(ServicioResponse::desde)
                .orElseThrow(() -> new RecursoNoEncontradoException("El servicio no existe"));
    }

    @Transactional(readOnly = true)
    public List<ServicioAdminResponse> listarTodos() {
        return servicioRepository.findAll(Sort.by("nombre"))
                .stream()
                .map(ServicioAdminResponse::desde)
                .toList();
    }

    @Transactional
    public ServicioAdminResponse crear(ServicioRequest datos) {
        String nombre = datos.nombre().trim();
        if (servicioRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ServicioYaExisteException("Ya existe un servicio con ese nombre");
        }
        Servicio servicio = new Servicio(nombre, limpiar(datos.descripcion()), datos.precio(),
                datos.unidadCobro(), datos.duracionMinutos());
        return ServicioAdminResponse.desde(servicioRepository.save(servicio));
    }

    @Transactional
    public ServicioAdminResponse actualizar(Long id, ServicioRequest datos) {
        Servicio servicio = buscar(id);
        String nombre = datos.nombre().trim();
        if (servicioRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new ServicioYaExisteException("Ya existe un servicio con ese nombre");
        }
        servicio.actualizarDatos(nombre, limpiar(datos.descripcion()), datos.precio(),
                datos.unidadCobro(), datos.duracionMinutos());
        return ServicioAdminResponse.desde(servicio);
    }

    @Transactional
    public ServicioAdminResponse cambiarEstado(Long id, boolean activo) {
        Servicio servicio = buscar(id);
        servicio.cambiarEstado(activo);
        return ServicioAdminResponse.desde(servicio);
    }

    private Servicio buscar(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El servicio no existe"));
    }

    private String limpiar (String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
