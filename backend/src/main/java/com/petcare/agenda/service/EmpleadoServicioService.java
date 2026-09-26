package com.petcare.agenda.service;

import com.petcare.agenda.domain.EmpleadoServicio;
import com.petcare.agenda.repository.EmpleadoServicioRepository;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.error.ReglaNegocioException;
import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.dto.ServicioResponse;
import com.petcare.servicios.repository.ServicioRepository;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServicioService {

    private static final String ROL_EMPLEADO = "EMPLEADO";

    private final EmpleadoServicioRepository empleadoServicioRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<ServicioResponse> consultar(Long empleadoId) {
        buscarEmpleado(empleadoId);
        return empleadoServicioRepository.findByEmpleadoIdOrderByServicioNombreAsc(empleadoId)
                .stream().map(es -> ServicioResponse.desde(es.getServicio())).toList();
    }

    @Transactional
    public List<ServicioResponse> reemplazar(Long empleadoId, List<Long> servicioIds) {
        Usuario empleado = buscarEmpleado(empleadoId);

        List<Long> unicos = servicioIds.stream().distinct().toList();

        List<Servicio> servicios = servicioRepository.findAllById(unicos);
        if (servicios.size() != unicos.size()) {
            throw new RecursoNoEncontradoException("Alguno de los servicios no existe");
        }

        Servicio inactivo = servicios.stream().filter(s -> !s.isActivo()).findFirst().orElse(null);
        if (inactivo != null) {
            throw new ReglaNegocioException("SERVICIO_INACTIVO",
                    "No se puede asignar el servicio desactivado: " + inactivo.getNombre());
        }

        empleadoServicioRepository.deleteByEmpleadoId(empleadoId);
        empleadoServicioRepository.flush();;

        List<EmpleadoServicio> nuevos = servicios.stream()
                .map(s -> new EmpleadoServicio(empleado, s))
                .toList();
        empleadoServicioRepository.saveAll(nuevos);

        return servicios.stream()
                .sorted(Comparator.comparing(Servicio::getNombre))
                .map(ServicioResponse::desde)
                .toList();
    }

    private Usuario buscarEmpleado(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getRoles().stream().anyMatch(r -> ROL_EMPLEADO.equals(r.getNombre())))
                .orElseThrow(() -> new RecursoNoEncontradoException("El empleado no existe"));
    }
}
