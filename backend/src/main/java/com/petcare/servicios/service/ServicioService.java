package com.petcare.servicios.service;

import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.servicios.dto.ServicioResponse;
import com.petcare.servicios.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
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
}
