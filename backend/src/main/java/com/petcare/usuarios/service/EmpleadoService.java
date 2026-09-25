package com.petcare.usuarios.service;

import com.petcare.common.error.CorreoYaRegistradoException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.usuarios.domain.Rol;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.dto.EmpleadoActualizarRequest;
import com.petcare.usuarios.dto.EmpleadoRequest;
import com.petcare.usuarios.dto.EmpleadoResponse;
import com.petcare.usuarios.repository.RolRepository;
import com.petcare.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private static final String ROL_EMPLEADO = "EMPLEADO";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder codificador;

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listar() {
        return usuarioRepository.findByRoles_NombreOrderByApellidoAscNombreAsc(ROL_EMPLEADO)
                .stream().map(EmpleadoResponse::desde).toList();
    }

    @Transactional
    public EmpleadoResponse crear(EmpleadoRequest datos) {
        String correo = datos.correo().trim().toLowerCase();
        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            throw new CorreoYaRegistradoException(correo);
        }
        Rol rolEmpleado = rolRepository.findByNombre(ROL_EMPLEADO)
                .orElseThrow(() -> new IllegalStateException("El rol EMPLEADO no existe"));

        Usuario empleado = new Usuario(
                datos.nombre().trim(), datos.apellido().trim(), correo,
                datos.telefono(), codificador.encode(datos.contrasenaTemporal()), null);
        empleado.asignarRol(rolEmpleado);

        return EmpleadoResponse.desde(usuarioRepository.save(empleado));
    }

    @Transactional
    public EmpleadoResponse actualizar(Long id, EmpleadoActualizarRequest datos) {
        Usuario empleado = buscarEmpleado(id);
        empleado.actualizarDatos(datos.nombre().trim(), datos.apellido().trim(), datos.telefono());
        return EmpleadoResponse.desde(empleado);
    }

    @Transactional
    public EmpleadoResponse cambiarEstado(Long id, boolean activo) {
        Usuario empleado = buscarEmpleado(id);
        empleado.cambiarEstado(activo);
        return EmpleadoResponse.desde(empleado);
    }

    private Usuario buscarEmpleado(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getRoles().stream().anyMatch(r -> ROL_EMPLEADO.equals(r.getNombre())))
                .orElseThrow(() -> new RecursoNoEncontradoException("El empleado no existe"));
    }
}
